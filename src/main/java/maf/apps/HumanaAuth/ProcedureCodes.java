package maf.apps.HumanaAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcedureCodes {
	public static List<List> getProcedures(String status, String serviceLine, String cptcodeFreq) {
		List<String> PTcpts = new ArrayList<>(Arrays.asList(new String[] { "97161", "97162", "97163", "97530", "97110",
				"97140", "97116", "97010", "97112", "97014" }));
		List<String> freq = new ArrayList<>(
				Arrays.asList(new String[] { "1", "1", "1", "36", "36", "12", "12", "12", "12", "12" }));

		List<String> OTcpts = new ArrayList<>(Arrays.asList(new String[] { "97165", "97166", "97167", "97530", "97110",
				"97014", "97140", "97010", "97012", "97035" }));

		List<String> PTcptsCont = new ArrayList<>(Arrays.asList(
				new String[] { "97164", "97530", "97110", "97140", "97112", "97116", "97010", "97014", "97035" }));

		List<String> OTcptsCont = new ArrayList<>(Arrays.asList(
				new String[] { "97168", "97530", "97110", "97140", "97112", "97010", "97014", "97035", "97022" }));

		List<List> procedure = new ArrayList<List>();
		if (status.equals("initial")) {

			if (serviceLine.equals("Physical Therapy")) {
				procedure = new ArrayList<List>();
				procedure.add(PTcpts);
				procedure.add(freq);
				return procedure;
			} else {
				procedure = new ArrayList<List>();
				procedure.add(OTcpts);
				procedure.add(freq);
				return procedure;
			}

		} else {
			int f = Integer.valueOf(cptcodeFreq.split("x")[1]);
			int series = Integer.valueOf(cptcodeFreq.split("x")[0]);

			if (serviceLine.equals("Physical Therapy")) {
				procedure = new ArrayList<List>();
				procedure.add(PTcptsCont);

			} else {
				procedure = new ArrayList<List>();
				procedure.add(OTcptsCont);

			}
			List<String> temp = new ArrayList<String>();
			String qty = "";
			for (int i = 0; i < 9; i++) {
				qty = (f * series) + "";
				if (i == 0) {

					temp.add("1");
				} else if (i > 0 && i < 3) {
					temp.add((f * series * 3) + "");

				} else {
					temp.add((f * series) + "");

				}

			}

			procedure.add(temp);
			return procedure;

		}

	}
}
