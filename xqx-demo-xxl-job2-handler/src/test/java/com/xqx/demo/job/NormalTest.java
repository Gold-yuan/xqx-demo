package com.xqx.demo.job;

import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.rpc.remoting.invoker.call.CallType;
import com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean;
import com.xxl.rpc.remoting.net.NetEnum;
import com.xxl.rpc.serialize.Serializer;

public class NormalTest {

    public static void main(String[] args) throws Exception {

        // param
        String jobHandler = "ReceiveBlacklistHandler";
//        jobHandler = "demoJobHandler";
        String params = "1";

        runTest(jobHandler, params);
    }

    /**
     * run jobhandler
     *
     * @param jobHandler
     * @param params
     */
    private static void runTest(String jobHandler, String params){
        // trigger data
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(1);
        triggerParam.setExecutorHandler(jobHandler);
        triggerParam.setExecutorParams(params);
        triggerParam.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.COVER_EARLY.name());
        triggerParam.setGlueType(GlueTypeEnum.BEAN.name());
        triggerParam.setGlueSource(null);
        triggerParam.setGlueUpdatetime(System.currentTimeMillis());
        triggerParam.setLogId(1);
        triggerParam.setLogDateTim(System.currentTimeMillis());

        // do remote trigger
        String accessToken = "passw0rd";
        ExecutorBiz executorBiz = (ExecutorBiz) new XxlRpcReferenceBean(NetEnum.JETTY, Serializer.SerializeEnum.HESSIAN.getSerializer(), CallType.SYNC,
                ExecutorBiz.class, null, 10000, "127.0.0.1:9996", accessToken, null).getObject();

        ReturnT<String> runResult = executorBiz.run(triggerParam);
        System.out.println("--------");
        System.out.println("--------");
        System.out.println("--------");
        System.out.println("--------");
        System.out.println("--------"+runResult);
        System.exit(0);
        
    }

	@Test
	public void executeJob() throws Exception {

		int p = 2;
		// httpclient
		HttpClient httpClient = null;
		httpClient = new HttpClient();
		httpClient.setFollowRedirects(false); // Configure HttpClient, for example:
		httpClient.start(); // Start HttpClient

		// request
		Request request = httpClient
				.newRequest("http://localhost:9062/xxl-job-admin/jobinfo/trigger?id=2&executorParam=" + p);
		request.method(HttpMethod.GET);
		request.timeout(5000, TimeUnit.MILLISECONDS);

		// invoke
		ContentResponse response = request.send();
		if (response.getStatus() != HttpStatus.OK_200) {
			System.out.println("111111");
		} else {
			System.out.println(222222);
		}

	}
}
