package org.playframework.playclipse.tests;

import java.util.List;
import org.playframework.playclipse.PlayPlugin;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.google.gson.Gson;

import fr.zenexity.pdt.swt.FancyDialog;

public class TestDialog extends FancyDialog {

	private String LISTURL = "http://localhost:9000/@tests?format=json";
	private Image logo;

	public TestDialog(Shell parentShell) {
		super(parentShell);
		getTestList();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.CLOSE_LABEL, true);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setSize(300, 300);
		super.configureShell(newShell);
	}

	private void buildTestList(String inputJson) {
//		{"seleniumTests":["Application.test.html"],"functionalTests":["ApplicationTest"],"unitTests":["BasicTest"]}
		System.out.println("buildTestList");
		Gson gson = new Gson();
		AllTests tests = gson.fromJson(inputJson, AllTests.class);
		System.out.println("Tests: {" + tests + "}");

		List<String> unitTests = tests.getUnitTests();
		(new Label(pageComposite, SWT.NONE)).setText("Unit tests");
		for (int i = 0; i < unitTests.size(); i++) {
			new TestControl(pageComposite, unitTests.get(i));
		}

		List<String> functionalTests = tests.getFunctionalTests();
		(new Label(pageComposite, SWT.NONE)).setText("Functional tests");
		for (int i = 0; i < functionalTests.size(); i++) {
			new TestControl(pageComposite, functionalTests.get(i));
		}

		// pageComposite.layout();
		refresh();
	}

	private void getTestList() {
		IHttpListener listener = new IHttpListener() {
			public void onSuccess(String result, String callId) {
				buildTestList(result);
			}
			public void onError(int status, String callId) {
				// TODO
				System.out.println("http Error!");
			}
		};
		HttpThread listCall = new HttpThread(listener, LISTURL, null);
		listCall.start();
	}

	// Classes for Gson deserialization

	static class AllTests {

		public AllTests() {}

		private List<String> unitTests;

		private List<String> functionalTests;

		private List<String> seleniumTests;

		public List<String> getUnitTests() {
			return unitTests;
		}

		public List<String> getFunctionalTests() {
			return functionalTests;
		}

		public List<String> getSeleniumTests() {
			return seleniumTests;
		}

		public String toString() {
			return "unitTests: " + unitTests + "; "
				  + "functionalTests: " + functionalTests + "; "
				  + "seleniumTests: " + seleniumTests + "; ";
		}

	}

}
