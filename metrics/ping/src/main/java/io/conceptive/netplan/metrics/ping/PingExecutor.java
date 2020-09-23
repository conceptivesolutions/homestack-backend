package io.conceptive.netplan.metrics.ping;

import com.zaxxer.ping.*;
import io.conceptive.netplan.core.model.Device;
import io.conceptive.netplan.metrics.api.*;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.*;

import javax.enterprise.context.ApplicationScoped;
import java.net.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Executes a "ping" / ICMP request to the given device
 *
 * @author w.glanzer, 18.09.2020
 */
@ApplicationScoped
public class PingExecutor implements IMetricsExecutor
{

  private static final int _COUNT = 1;

  @Override
  public boolean canExecute()
  {
    return SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC;
  }

  @NotNull
  @Override
  public IMetricsResult execute(@NotNull Device pDevice)
  {
    List<_PingResultObject> results = new ArrayList<>();
    Flowable.create(new _PingFlowable(pDevice), BackpressureStrategy.LATEST)
        .limit(_COUNT)
        .blockingForEach(results::add);
    return new _PingResult(results);
  }

  /**
   * Executes a continous ping request - dispose to stop
   */
  private static class _PingFlowable implements FlowableOnSubscribe<_PingResultObject>, PingResponseHandler
  {
    private final IcmpPinger pinger = new IcmpPinger(this);
    private final Device device;
    private final AtomicInteger seqCount = new AtomicInteger(0);
    private FlowableEmitter<_PingResultObject> emitter;

    public _PingFlowable(@NotNull Device pDevice)
    {
      device = pDevice;
    }

    @Override
    public void subscribe(@NotNull FlowableEmitter<_PingResultObject> pEmitter)
    {
      emitter = pEmitter;
      emitter.setDisposable(new Disposable()
      {
        private boolean disposed = false;

        @Override
        public void dispose()
        {
          if(!disposed)
            pinger.stopSelector(); // force
          disposed = true;
        }

        @Override
        public boolean isDisposed()
        {
          return disposed;
        }
      });

      // execute first ping
      _doPing();

      pinger.runSelector();
    }

    @Override
    public void onResponse(@NotNull PingTarget pPingTarget, double pResponseTimeSec, int pByteCount, int pSeq)
    {
      emitter.onNext(new _PingResultObject(pPingTarget, (float) (pResponseTimeSec * 1000), seqCount.getAndIncrement()));
      _doPing();
    }

    @Override
    public void onTimeout(@NotNull PingTarget pPingTarget)
    {
      emitter.onNext(new _PingResultObject(pPingTarget, -1, seqCount.getAndIncrement()));
      _doPing();
    }

    /**
     * Executes a single ping request
     */
    private void _doPing()
    {
      try
      {
        pinger.ping(new PingTarget(InetAddress.getByName(device.address), null, 5_000));
      }
      catch (UnknownHostException e)
      {
        emitter.onError(e);
      }
    }
  }

  /**
   * Simple POJO for ping result
   */
  private static class _PingResultObject 
  {
    private final PingTarget target;
    private final float responseTime; // -1 for timeout
    private final int seq;

    public _PingResultObject(@NotNull PingTarget pTarget, float pResponseTime, int pSeq)
    {
      target = pTarget;
      responseTime = pResponseTime;
      seq = pSeq;
    }

    @Override
    public String toString()
    {
      return "_PingResultObject{" +
          "target=" + target.getInetAddress() +
          ", responseTime=" + responseTime +
          ", seq=" + seq +
          '}';
    }
  }

  /**
   * Result-Impl
   */
  private static class _PingResult implements IMetricsResult
  {
    private final EState state;
    private final String stateDescription;

    public _PingResult(@NotNull List<_PingResultObject> pResults)
    {
      state = _getState(pResults);
      stateDescription = pResults.stream()
          .map(_PingResultObject::toString)
          .collect(Collectors.joining("\n"));
    }

    @NotNull
    @Override
    public EState getState()
    {
      return state;
    }

    @Nullable
    @Override
    public String getStateDescription()
    {
      return stateDescription;
    }

    @Override
    public String toString()
    {
      return "_PingResult{" +
          ", state=" + state +
          ", stateDescription='" + stateDescription + '\'' +
          '}';
    }

    /**
     * Returns the appropriate state
     *
     * @param pResults all ping results
     * @return the state
     */
    @NotNull
    private static EState _getState(@NotNull List<_PingResultObject> pResults)
    {
      if(pResults.isEmpty())
        return EState.UNKNOWN;
      if (pResults.stream().allMatch(pResult -> pResult.responseTime >= 0))
        return EState.SUCCESS;
      else if (pResults.stream().anyMatch(pResult -> pResult.responseTime >= 0))
        return EState.WARNING;
      else
        return EState.FAILURE;
    }
  }

}