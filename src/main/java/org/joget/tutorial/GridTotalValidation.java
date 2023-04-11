package org.joget.tutorial;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormMultiRowValidator;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.service.FormUtil;
import org.joget.commons.util.LogUtil;

public class GridTotalValidation extends FormMultiRowValidator {

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return this.getClass().getName();
	}

	@Override
	public String getLabel() {
		return "Training - Grid Total Validation Plugin";
	}

	@Override
	public String getPropertyOptions() {
		return AppUtil.readPluginResource(getClassName(), "properties/GridTotalValidation.json", null, true, null);
	}

	@Override
	public String getDescription() {
		return "Form validator untuk memvalidasi nilai total price Purchase request";
	}

	@Override
	public String getName() {
		return "Training - Grid Total Validation Plugin";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public boolean validate(Element element, FormData data, FormRowSet rows) {
		boolean result = true;
		// 1. Ambil data dari properti: mandatory, maxtotal, minTotal
		String mandatory = getPropertyString("mandatory");
		String maxTotal = getPropertyString("maxTotal");
		String minTotal = getPropertyString("minTotal");
		String idElement = FormUtil.getElementParameterName(element);
		// 2. Cek mandatory
		if ("true".equals(mandatory)) {
			int checkRow = 0;
			for (FormRow row : rows) {
				if (!"".equals(row.getProperty("item"))) {
					checkRow++;
				}
			}

			if (checkRow <= 0) {
				data.addFormError(idElement, "Items wajib diisi!");
				LogUtil.info(getClassName(), "Rows kosong");
				result = false;
			}
		}

		// 3. Pengecekan total price
		if (rows != null && !rows.isEmpty()) {
			double total = 0;

			// Sum the values from column "price"
			for (FormRow row : rows) {
				try {
					double amount = Double.parseDouble(row.getProperty("price")) * Double.parseDouble(row.getProperty("quantity"));
					total += amount;
				} catch (Exception e) {
				}
			}

			LogUtil.info(getClassName(), "Total : " + total);

			if (total > Double.valueOf(maxTotal)) {
				data.addFormError(idElement, "Nilai lebih besar dari batas atas");
				result = false;
			} else if (total < Double.valueOf(minTotal)) {
				data.addFormError(idElement, "Nilai lebih kecil dari batas bawah");
				result = false;
			}
		}

		return result;
	}

}
