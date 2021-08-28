package main.java.client.testcase;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import main.java.Analyzer;
import main.java.MyConfig;
import main.java.analyze.utils.ConstantUtils;
import main.java.analyze.utils.output.FileUtils;
import main.java.client.obj.model.ictg.ICCMsg;
import main.java.client.obj.model.ictg.IntentRecieveModel;
import main.java.client.testcase.TestGenerationProcess;

/**
 * single null value test case generation
 *
 */
public class TestGenerator extends Analyzer {
	Set<String> iccAll = new HashSet<String>();
	Map<String, String> key2Value = new HashMap<String, String>();

	public TestGenerator() {
		super();
		init();
	}

	/**
	 * file operation before analysis
	 */
	private void init() {
		String testCasefolder = MyConfig.getInstance().getResultFolder() + ConstantUtils.TESTCASEFOLDER
				+ appModel.getAppName() + File.separator;
		String appProjectFolder = MyConfig.getInstance().getResultFolder() + ConstantUtils.TESTCASEFOLDER
				+ appModel.getAppName() + File.separator + ConstantUtils.GENERATEDAPPFOLDER;
		String iccMsg = MyConfig.getInstance().getResultFolder() + ConstantUtils.TESTCASEFOLDER + appModel.getAppName()
				+ File.separator + "testCase.iccmsg";

		FileUtils.createFolder(testCasefolder);
		FileUtils.delFolder(appProjectFolder);
		FileUtils.delFile(iccMsg);
	}

	@Override
	public void analyze() {
		System.out.println("Start Test Generation...");

		TestGenerationProcess tg = new TestGenerationProcess(appModel);
		tg.createAndroidProject();

		// className : act to be analyzed
		for (String className : appModel.getToBeAnalyzedActivityMap().keySet()) {
			Set<ICCMsg> ICCs = getIccs(className);

			if (ICCs != null && ICCs.size() != 0) {
				tg.handleICCMsgs(ICCs, className);
			}
		}
		tg.generateManifest();
		tg.buildProject();

		System.out.println("End Test Generation...");
	}

	/**
	 * ICC msg generation (***)
	 * 
	 * @param className
	 * @return
	 */
	private Set<ICCMsg> getIccs(String className) {
		Set<ICCMsg> ICCs = new HashSet<ICCMsg>();

		Set<ICCMsg> ICC_NVs = IntentRecieveModel.getUsedACDTStrSNV(className);
		ICCs.addAll(ICC_NVs);

		Set<ICCMsg> ICC_AUs = IntentRecieveModel.getUsedACDTStrAU(className);
		ICCs.addAll(ICC_AUs);

		// add par and ser without parameter
		ICCMsg icc1 = new ICCMsg("");
		icc1.getExtra().add("Parcelable-\"parObj\"");
		ICCs.add(icc1);

		ICCMsg icc2 = new ICCMsg("");
		icc2.getExtra().add("Serializable-\"serObj\"");
		ICCs.add(icc2);

		return ICCs;
	}

}
