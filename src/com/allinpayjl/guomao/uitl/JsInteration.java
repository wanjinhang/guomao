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
	 * 消费
	 * @param price 金额
	 * @param payType 支付方式
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
			data.putValue(RequestData.AMOUNT, a);// 金额（12位，分为单位，不足左补0）
			if(payType.equals("qrcode")){
				data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_QRCODE);// 银行卡交易
			}else{
				data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_BANKCARD);// 银行卡交易
			}
			
			data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_SALE);// 消费操作;
			bundle.putSerializable(RequestData.KEY_ERTRAS, data);
			intent.putExtras(bundle);
			activity.startActivityForResult(intent, 0);

		} catch (Exception e) {
			Toast.makeText(myContext, "未安装支付控件,请先安装相应支付控件", Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * 更多操作
	 * @param type 操作类型
	 */
	@JavascriptInterface
	public void allinpay_more(String type){
		try {
			Intent intent = new Intent();
			intent.setComponent(new ComponentName("com.allinpay.usdk", "com.allinpay.usdk.MainActivity"));

			Bundle bundle = new Bundle();
			RequestData data = new RequestData();
			data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_BANKCARD);// 银行卡交易
			switch (type) {
			case "login":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_LOGON);// 签到;
				break;
			case "balance":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_GET_BALANCE);// 查银行卡余额;
				break;
			case "detail":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_GET_DETAIL);// 显示交易明细;
				break;
			case "settle":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_SETTLE);//结算
				break;
			case "reprint_settle":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_REPRINT_SETTLE);//重打结算
				break;
			case "print_total":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_PRINT_TOTAL);//打印总汇
				break;
			case "print_detail":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_PRINT_DETAIL);//打印明细
				break;
			case "reprint":
				data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_REPRINT);//打印末笔
				data.putValue(RequestData.ORIG_TRACE_NO, "000000");//打印末笔
				break;
			default:
				break;
			}
			
			
			bundle.putSerializable(RequestData.KEY_ERTRAS, data);
			intent.putExtras(bundle);
			activity.startActivityForResult(intent, 1);

		} catch (Exception e) {
			Toast.makeText(myContext, "未安装支付控件,请先安装相应支付控件", Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * 消费撤销   
	 * @param code  凭证号
	 * @param type  消费类型
	 */
	@JavascriptInterface
	public void allinpay_void(String code,String type){
		
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("com.allinpay.usdk", "com.allinpay.usdk.MainActivity"));

		Bundle bundle = new Bundle();
		RequestData data = new RequestData();
		if(type.equals("qrcode")){
			data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_QRCODE);// 扫码交易
		}else{
			data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_BANKCARD);// 银行卡交易
		}
		
		data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_VOID);// 消费撤销
		data.putValue(RequestData.ORIG_TRACE_NO, code);// 凭证号
		bundle.putSerializable(RequestData.KEY_ERTRAS, data);
		intent.putExtras(bundle);
		activity.startActivityForResult(intent, 2);
	}
	/**
	 * 打印指定交易
	 * @param code  凭证号
	 */
	@JavascriptInterface
	public void allinpay_reprint(String code){
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("com.allinpay.usdk", "com.allinpay.usdk.MainActivity"));

		Bundle bundle = new Bundle();
		RequestData data = new RequestData();
		data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_BANKCARD);// 银行卡交易
		data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_REPRINT);//打印
		data.putValue(RequestData.ORIG_TRACE_NO, code);//打印指定交易
		bundle.putSerializable(RequestData.KEY_ERTRAS, data);
		intent.putExtras(bundle);
		activity.startActivityForResult(intent, 1);
		
	}
	/**
	 * 退货
	 * @param type  消费类型
	 * @param amount 金额
	 * @param ref_no 参考号
	 * @param date 交易日期
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
			data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_QRCODE);// 扫码交易
			data.putValue(RequestData.ORIG_TRACE_NO, ref_no);
		}else{
			data.putValue(RequestData.CARDTYPE, BaseData.CARDTYPE_BANKCARD);// 银行卡交易
			data.putValue(RequestData.TRANSTYPE, BaseData.TRANSTYPE_REFUND);//退货
			data.putValue(RequestData.AMOUNT,a );
			data.putValue(RequestData.ORIG_REF_NO, ref_no);//参考号
			data.putValue(RequestData.ORIG_DATE, date);//原交易日期
		}
		
		bundle.putSerializable(RequestData.KEY_ERTRAS, data);
		intent.putExtras(bundle);
		activity.startActivityForResult(intent, 2);
		
	}
	
	//版本更新
	@JavascriptInterface
	public void allinpay_download(){
		Toast.makeText(myContext, "版本更新", Toast.LENGTH_LONG).show();
		final Uri uri = Uri.parse(Url.upDataUrl);         
		final Intent it = new Intent(Intent.ACTION_VIEW, uri);         
		myContext.startActivity(it);
		
	}
	//数据上传
	@JavascriptInterface 
	public void dataShow(){
		Intent intent = new Intent(myContext,DataActivity.class);
		myContext.startActivity(intent);
	}
	//测试
	@JavascriptInterface
	public void test(){
		String json="{\"map\":{\"SIGN\":\"38639f056c961fd8bd71ddf01af297de\",\"REJCODE_CN\":\"交易成功\",\"REF_NO\":\"001476955679\",\"BATCH_NO\":\"000024\",\"TRANS_CHANNEL\":\"006\",\"AUTH_NO\":\"012345\",\"ORIG_DATE\":\"\",\"MEMO\":\"\",\"EXP_DATE\":\"2703\",\"CARDNO\":\"6222034200000171108\",\"ISS_NO\":\"\",\"ORIG_TRACE_NO\":\"\",\"TRANSTYPE\":\"002\",\"ORIG_REF_NO\":\"\",\"CARDTYPE\":\"001\",\"MERCH_NAME\":\"通联支付吉林分公司收银宝测试\",\"TIME\":\"085920\",\"BUSINESS_ID\":\"100100001\",\"ISS_NAME\":\"\",\"PRINT_FLAG\":\"1\",\"AMOUNT\":\"000000000001\",\"REJCODE\":\"00\",\"MERCH_ID\":\"990241048166000\",\"OPER_NO\":\"01\",\"TER_ID\":\"00000002\",\"DATE\":\"0505\",\"TRACE_NO\":\"001252\",\"TRANS_CHECK\":\"\"}}";
		DBManager dbManager=new DBManager(myContext);
		dbManager.insert(json,1);
	}
	
	
	
}
