package cn.ddshcool.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {

	public static SpannableString getPostContent(final Context context, final TextView tv, String source) {
		//表情正则表达式
		String regexEmoji = "\\[[\u4e00-\u9fa5\\w]+\\]";

		SpannableString spannableString = new SpannableString(source);
		
		Pattern pattern = Pattern.compile(regexEmoji);
		Matcher matcher = pattern.matcher(spannableString);
		
		if(matcher.find()) {
			//只获得一次实例,优化查询方法
			tv.setMovementMethod(LinkMovementMethod.getInstance());
			matcher.reset();
		}

		//开始匹配目标文字
		while(matcher.find()) {
			String emojiStr = matcher.group();

			if(emojiStr != null) {
				int start = matcher.start();
				
				int imgRes = EmotionUtils.getImgByName(emojiStr);
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes);
				
				if(bitmap != null) {
					int size = (int) tv.getTextSize();
					bitmap = Bitmap.createScaledBitmap(bitmap, size + 10, size + 10, true);
					
					ImageSpan imageSpan = new ImageSpan(context, bitmap);
					spannableString.setSpan(imageSpan, start, start + emojiStr.length(), 
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}

		}

		return spannableString;
	}

	public static SpannableString getPostContent(final Context context, final EditText tv, String source) {

		String regexEmoji = "\\[[\u4e00-\u9fa5\\w]+\\]";


		SpannableString spannableString = new SpannableString(source);

		Pattern pattern = Pattern.compile(regexEmoji);
		Matcher matcher = pattern.matcher(spannableString);

		if(matcher.find()) {
			tv.setMovementMethod(LinkMovementMethod.getInstance());
			matcher.reset();
		}

		while(matcher.find()) {
			String emojiStr = matcher.group();

			if(emojiStr != null) {
				int start = matcher.start();

				int imgRes = EmotionUtils.getImgByName(emojiStr);
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes);

				if(bitmap != null) {
					int size = (int) tv.getTextSize();
					bitmap = Bitmap.createScaledBitmap(bitmap, size + 5, size + 5, true);

					ImageSpan imageSpan = new ImageSpan(context, bitmap);
					spannableString.setSpan(imageSpan, start, start + emojiStr.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}

		}


		return spannableString;
	}

}
