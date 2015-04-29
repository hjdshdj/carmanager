package com.yu.car_android.bean;

import java.io.Serializable;

/**
 * 本地文件信息，用于計算文件緩存大小
 * @author yanjiali jiali_yan1986@163.com 2012-8-15
 */
public class LocalFileInfo implements Serializable {

	private static final long serialVersionUID = 1715015780627913942L;
	/**文件路径*/
	public String filePath;
	/**文件大小*/
	public long size;
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		return this.filePath.equals(((LocalFileInfo) o).filePath);
	}
	@Override
	public int hashCode() {
		return filePath.hashCode();
	}
}
