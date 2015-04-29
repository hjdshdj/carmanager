package com.yu.car_android.bean;

import java.io.Serializable;


/**
 * 优惠信息
 * 
 * @author u
 * 
 */
public class Favorable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6959249205078367899L;
	// Id 返回这条优惠信息id
	private String Id;
	// activity_title 活动主题
	private String activity_title;
	// activity_desc 活动简述
	private String activity_desc;
	// activity_content 活动内容
	private String activity_content;
	// activity_detaile 活动细节
	private String activity_detaile;
	// activity_title_url 活动标题图片url
	private String activity_title_url;
	// activity_content_url 活动内容图片url
	private String activity_content_url;
	// bank_id 银行Id
	private String bank_id;
	// bank_name 银行名称
	private String bank_name;
	// logo 银行logo
	private String logo;
	// sectors_icon 优惠类型icon名称
	private String sectors_icon;
	// bc_name 所属地域
	private String bc_name;
	// activity_type 活动方式（如：赠券、礼品）
	private String activity_type;
	// sectors_id 所属分类Id
	private String sectors_id;
	// heat 关注热度
	private int heat;
	// activity_start_date 活动开始时间（格式：yyyy-MM-dd）
	private String activity_start_date;
	// activity_end_date 活动结束时间（格式：yyyy-MM-dd）
	private String activity_end_date;
	// create_time 创建时间（格式：yyyy-MM-dd hh24:mi:ss）
	private String create_time;
	// isFavorties 0否、1以收藏
	private int isFavorties;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getActivity_title() {
		return activity_title;
	}

	public void setActivity_title(String activity_title) {
		this.activity_title = activity_title;
	}

	public String getActivity_desc() {
		return activity_desc;
	}

	public void setActivity_desc(String activity_desc) {
		this.activity_desc = activity_desc;
	}

	public String getActivity_content() {
		return activity_content;
	}

	public void setActivity_content(String activity_content) {
		this.activity_content = activity_content;
	}

	public String getActivity_detaile() {
		return activity_detaile;
	}

	public void setActivity_detaile(String activity_detaile) {
		this.activity_detaile = activity_detaile;
	}

	public String getActivity_title_url() {
		return activity_title_url;
	}

	public void setActivity_title_url(String activity_title_url) {
		this.activity_title_url = activity_title_url;
	}

	public String getActivity_content_url() {
		return activity_content_url;
	}

	public void setActivity_content_url(String activity_content_url) {
		this.activity_content_url = activity_content_url;
	}

	public String getBank_id() {
		return bank_id;
	}

	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getSectors_icon() {
		return sectors_icon;
	}

	public void setSectors_icon(String sectors_icon) {
		this.sectors_icon = sectors_icon;
	}

	public String getBc_name() {
		return bc_name;
	}

	public void setBc_name(String bc_name) {
		this.bc_name = bc_name;
	}

	public String getActivity_type() {
		return activity_type;
	}

	public void setActivity_type(String activity_type) {
		this.activity_type = activity_type;
	}

	public String getSectors_id() {
		return sectors_id;
	}

	public void setSectors_id(String sectors_id) {
		this.sectors_id = sectors_id;
	}

	public int getHeat() {
		return heat;
	}

	public void setHeat(int heat) {
		this.heat = heat;
	}

	public String getActivity_start_date() {
		return activity_start_date;
	}

	public void setActivity_start_date(String activity_start_date) {
		this.activity_start_date = activity_start_date;
	}

	public String getActivity_end_date() {
		return activity_end_date;
	}

	public void setActivity_end_date(String activity_end_date) {
		this.activity_end_date = activity_end_date;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public int getIsFavorties() {
		return isFavorties;
	}

	public void setIsFavorties(int isFavorties) {
		this.isFavorties = isFavorties;
	}

}
