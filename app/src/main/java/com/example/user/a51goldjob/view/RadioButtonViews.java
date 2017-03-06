package com.example.user.a51goldjob.view;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chyjr.goldjob.fr.utils.CollectionUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.activity.BaseActivity;

/**
 * RadioButton 帮助类
 * 
 * @author yeq
 * 
 */
public class RadioButtonViews {

	/**
	 * Radio数据接口
	 * 
	 * @author yeq
	 * 
	 */
	public static interface IRadioItem<T> {
		String getText();
		T getTag();
		boolean isChecked();
	}

	public static class RadioButtonViewHelper {

		private BaseActivity activity;
		private RadioGroup radioGroup;
		private LayoutInflater layoutInflater;

		public static RadioButtonViewHelper create(BaseActivity activity,
				RadioGroup radioGroup) {
			return new RadioButtonViewHelper(activity, radioGroup);
		}

		public RadioButtonViewHelper(BaseActivity activity,
				RadioGroup radioGroup) {
			this.activity = activity;
			this.radioGroup = radioGroup;
			this.layoutInflater = activity.getLayoutInflater();
		}

		/**
		 * 创建一组RadioButton
		 * 
		 * @param radioItems
		 */
		public <T> void createRadioButtons(List<IRadioItem<T>> radioItems) {
			createRadioButtons(radioItems, R.layout.radiobutton);
		}

		/**
		 * 创建一组RadioButton
		 * 
		 * @param radioItems
		 * @param resource
		 */
		public <T> void createRadioButtons(List<IRadioItem<T>> radioItems, int resource) {
			if (CollectionUtils.isEmpty(radioItems) || resource <= 0) {
				return;
			}
			String textValue = null;
			IRadioItem<T> radioItem = null;
			RadioButton radioButton = null, firstRadioButton = null;
			boolean checked = false, hasChecked = false;
			for (int i = 0; i < radioItems.size(); i++) {
				radioItem = radioItems.get(i);
				radioButton = (RadioButton) layoutInflater.inflate(resource,
						null);
				if (i == 0) {
					firstRadioButton = radioButton;
				}
				textValue = radioItem.getText();
				if (StringUtils.isEmpty(textValue)) {
					continue;
				}
				radioButton.setText(textValue);

				radioButton.setTag(radioItem.getTag());
				
				checked = radioItem.isChecked();
				if (checked) {
					hasChecked = checked;
				}
				radioButton.setChecked(checked);

				radioGroup.addView(radioButton);
			}

			if (!(hasChecked) && firstRadioButton != null) {
				firstRadioButton.setChecked(true);
			}
		}

		/**
		 * 创建一组RadioButton
		 * 
		 * @param radioItems
		 * @param textKey
		 * @param checkKey
		 */
		public void createRadioButtons(List<Map<String, Object>> radioItems,
				String textKey, String tagKey, String checkKey) {
			createRadioButtons(radioItems, R.layout.radiobutton, textKey, tagKey, checkKey);
		}

		/**
		 * 创建一组RadioButton
		 * 
		 * @param radioItems
		 * @param resource
		 * @param textKey
		 * @param checkKey
		 */
		public void createRadioButtons(List<Map<String, Object>> radioItems,
				int resource, String textKey, String tagKey, String checkKey) {
			if (CollectionUtils.isEmpty(radioItems) || resource <= 0
					|| StringUtils.isEmpty(textKey)) {
				return;
			}
			String textValue = null;
			Map<String, Object> radioItem = null;
			RadioButton radioButton = null, firstRadioButton = null;
			boolean checked = false, hasChecked = false;
			for (int i = 0; i < radioItems.size(); i++) {
				radioItem = radioItems.get(i);
				radioButton = (RadioButton) layoutInflater.inflate(resource, null);
				if (i == 0) {
					firstRadioButton = radioButton;
				}
				
				textValue = String.valueOf(radioItem.get(textKey));
				if (StringUtils.isEmpty(textValue)) {
					continue;
				}
				radioButton.setText(textValue);
				
				if (StringUtils.isNotEmpty(tagKey)) {
					radioButton.setTag(radioItem.get(tagKey));
				}
				
				radioGroup.addView(radioButton);
				if (StringUtils.isNotEmpty(checkKey)) {
					checked = BooleanUtils.toBoolean((String) radioItem.get(checkKey));
				}
				
				if (checked) {
					hasChecked = true;
				}
				
				radioButton.setChecked(checked);
			}

			if (!(hasChecked) && firstRadioButton != null) {
				firstRadioButton.setChecked(true);
			}
		}
		
		public RadioButton getSelectedItem() {
			return (RadioButton) activity.findViewById(radioGroup.getCheckedRadioButtonId());
		}
		
		public Object getSelectedItemTag() {
			RadioButton radioButton = getSelectedItem();
			return getRadioButtonTag(radioButton);
		}
		
		public String getSelectedItemText() {
			RadioButton radioButton = getSelectedItem();
			return getRadioButtonText(radioButton);
		}
		
		public String getRadioButtonText(RadioButton radioButton) {
			return radioButton == null ? null : String.valueOf(radioButton.getText());
		}
		
		public Object getRadioButtonTag(RadioButton radioButton) {
			return radioButton == null ? null : radioButton.getTag();
		}
	}
}
