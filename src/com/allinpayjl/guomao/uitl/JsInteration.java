package com.allinpayjl.guomao.uitl;

import com.allinpay.usdk.core.data.BaseData;
import com.allinpay.usdk.core.data.RequestData;
import com.allinpayjl.guomao.DAO.DBManager;
import com.allinpayjl.guomao.activity.DataActivity;
import com.allinpayjl.guomao.activity.MainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class JsInteration {

	public Context myContext;
	public Activity activity;
	public WebView myWebView;
	public JsInteration(MainActivity context, WebView myWebView) {
		// TODO Auto-generated constructor stub
		this.myContext = context;
		this.activity = (Activity) context;
		
		this.myWebView=myWebView;
		
	}

	/**
	 * ����
	 * @param price ���
	 * @param payType ֧����ʽ
	 */
	@JavascriptInterface
	public void allinpay_pay( String price,String payType) {
		
		price= price.replace(".", "");
		int i =Integer.parseInt(price);
		String a = String.format("%012d", i);
		try {
			Intent intent = new Intent();
			intent.setComponent(new ComponentName("com.allinpay.usdk", "com.allinpay.usdk.MainActivity"));

			Bundle bundle = new Bundle();
			RequestData data = new RequestData();
			data.putValue(RequestData.AMOUNT, a);// ��12λ����Ϊ��λ��������0��
			if(payType.equals("qrcode")){
				data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_QRCODE);// ���п�����
			}else{
				data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_BANKCARD);// ���п�����
			}
			
			data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_SALE);// ���Ѳ���;
			bundle.putSerializable(RequestData.KEY_ERTRAS, data);
			intent.putExtras(bundle);
			activity.startActivityForResult(intent, 0);

		} catch (Exception e) {
			Toast.makeText(myContext, "δ��װ֧���ؼ�,���Ȱ�װ��Ӧ֧���ؼ�", Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * �������
	 * @param type ��������
	 */
	@JavascriptInterface
	public void allinpay_more(String type){
		try {
			Intent intent = new Intent();
			intent.setComponent(new ComponentName("com.allinpay.usdk", "com.allinpay.usdk.MainActivity"));

			Bundle bundle = new Bundle();
			RequestData data = new RequestData();
			data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_BANKCARD);// ���п�����
			switch (type) {
			case "login":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_LOGON);// ǩ��;
				break;
			case "balance":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_GET_BALANCE);// �����п����;
				break;
			case "detail":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_GET_DETAIL);// ��ʾ������ϸ;
				break;
			case "settle":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_SETTLE);//����
				break;
			case "reprint_settle":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_REPRINT_SETTLE);//�ش����
				break;
			case "print_total":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_PRINT_TOTAL);//��ӡ�ܻ�
				break;
			case "print_detail":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_PRINT_DETAIL);//��ӡ��ϸ
				break;
			case "reprint":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_REPRINT);//��ӡĩ��
				data.putValue(RequestData.ORIG_TRACE_NO, "000000");//��ӡĩ��
				break;
			default:
				break;
			}
			
			
			bundle.putSerializable(RequestData.KEY_ERTRAS, data);
			intent.putExtras(bundle);
			activity.startActivityForResult(intent, 1);

		} catch (Exception e) {
			Toast.makeText(myContext, "δ��װ֧���ؼ�,���Ȱ�װ��Ӧ֧���ؼ�", Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * ���ѳ���   
	 * @param code  ƾ֤��
	 * @param type  ��������
	 */
	@JavascriptInterface
	public void allinpay_void(String code,String type){
		
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("com.allinpay.usdk", "com.allinpay.usdk.MainActivity"));

		Bundle bundle = new Bundle();
		RequestData data = new RequestData();
		if(type.equals("qrcode")){
			data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_QRCODE);// ɨ�뽻��
		}else{
			data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_BANKCARD);// ���п�����
		}
		
		data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_VOID);// ���ѳ���
		data.putValue(RequestData.ORIG_TRACE_NO, code);// ƾ֤��
		bundle.putSerializable(RequestData.KEY_ERTRAS, data);
		intent.putExtras(bundle);
		activity.startActivityForResult(intent, 2);
	}
	/**
	 * ��ӡָ������
	 * @param code  ƾ֤��
	 */
	@JavascriptInterface
	public void allinpay_reprint(String code){
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("com.allinpay.usdk", "com.allinpay.usdk.MainActivity"));

		Bundle bundle = new Bundle();
		RequestData data = new RequestData();
		data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_BANKCARD);// ���п�����
		data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_REPRINT);//��ӡ
		data.putValue(RequestData.ORIG_TRACE_NO, code);//��ӡָ������
		bundle.putSerializable(RequestData.KEY_ERTRAS, data);
		intent.putExtras(bundle);
		activity.startActivityForResult(intent, 1);
		
	}
	/**
	 * �˻�
	 * @param type  ��������
	 * @param amount ���
	 * @param ref_no �ο���
	 * @param date ��������
	 */
	@JavascriptInterface
	public void allinpay_refund(String type,String amount,String ref_no,String date){
		amount= amount.replace(".", "");
		int i =Integer.parseInt(amount);
		String a = String.format("%012d", i);
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("com.allinpay.usdk", "com.allinpay.usdk.MainActivity"));

		Bundle bundle = new Bundle();
		RequestData data = new RequestData();
		if(type.equals("qrcode")){
			data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_QRCODE);// ɨ�뽻��
			data.putValue(RequestData.ORIG_TRACE_NO, ref_no);
		}else{
			data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_BANKCARD);// ���п�����
			data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_REFUND);//�˻�
			data.putValue(RequestData.AMOUNT,a );
			data.putValue(RequestData.ORIG_REF_NO, ref_no);//�ο���
			data.putValue(RequestData.ORIG_DATE, date);//ԭ��������
		}
		
		bundle.putSerializable(RequestData.KEY_ERTRAS, data);
		intent.putExtras(bundle);
		activity.startActivityForResult(intent, 2);
		
	}
	
	//�汾����
	@JavascriptInterface
	public void allinpay_download(){
		Toast.makeText(myContext, "�汾����", Toast.LENGTH_LONG).show();
		final Uri uri = Uri.parse(Url.upDataUrl);         
		final Intent it = new Intent(Intent.ACTION_VIEW, uri);         
		myContext.startActivity(it);
		
	}
	//�����ϴ�
	@JavascriptInterface 
	public void dataShow(){
		Intent intent = new Intent(myContext,DataActivity.class);
		myContext.startActivity(intent);
	}
	//����
	@JavascriptInterface
	public void test(){
		String json="{\"map\":{\"SIGN\":\"38639f056c961fd8bd71ddf01af297de\",\"REJCODE_CN\":\"���׳ɹ�\",\"REF_NO\":\"001476955679\",\"BATCH_NO\":\"000024\",\"TRANS_CHANNEL\":\"006\",\"AUTH_NO\":\"012345\",\"ORIG_DATE\":\"\",\"MEMO\":\"\",\"EXP_DATE\":\"2703\",\"CARDNO\":\"6222034200000171108\",\"ISS_NO\":\"\",\"ORIG_TRACE_NO\":\"\",\"TRANSTYPE\":\"002\",\"ORIG_REF_NO\":\"\",\"CARDTYPE\":\"001\",\"MERCH_NAME\":\"ͨ��֧�����ַֹ�˾����������\",\"TIME\":\"085920\",\"BUSINESS_ID\":\"100100001\",\"ISS_NAME\":\"\",\"PRINT_FLAG\":\"1\",\"AMOUNT\":\"000000000001\",\"REJCODE\":\"00\",\"MERCH_ID\":\"990241048166000\",\"OPER_NO\":\"01\",\"TER_ID\":\"00000002\",\"DATE\":\"0505\",\"TRACE_NO\":\"001252\",\"TRANS_CHECK\":\"\"}}";
		DBManager dbManager=new DBManager(myContext);
		dbManager.insert(json,1);
	}
	
	
	
}
