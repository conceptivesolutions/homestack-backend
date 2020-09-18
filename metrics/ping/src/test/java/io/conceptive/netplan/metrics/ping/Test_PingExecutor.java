package io.conceptive.netplan.metrics.ping;

import io.conceptive.netplan.core.model.Device;
import io.conceptive.netplan.metrics.api.*;
import org.junit.*;

/**
 * Test for PingExecutor
 *
 * @see PingExecutor
 * @author w.glanzer, 18.09.2020
 */
public class Test_PingExecutor
{

  private IMetricsExecutor executor;
  private Device device;

  @Before
  public void setUp()
  {
    executor = new PingExecutor();
    device = new Device();
    device.address = "1.1.1.1";
    device.id = "__dummy__";
  }

  @Test
  public void test_execute()
  {
    Assert.assertTrue(executor.canExecute());
    IMetricsResult result = executor.execute(device);
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getState());
  }

}
