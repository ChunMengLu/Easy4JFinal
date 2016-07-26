package net.dreamlu.easy.commons.result;

import com.jfinal.plugin.activerecord.Page;

import java.io.Serializable;
import java.util.List;

public class DataTables<T> implements Serializable {

	private long draw;
	private long recordsTotal;
	private long recordsFiltered;
	private List<T> data;

	public DataTables(int draw, Page<T> page) {
		this.draw = draw;
		this.recordsTotal = page.getTotalRow();
		this.recordsFiltered = page.getTotalRow();
		this.data = page.getList();
	}

	public long getDraw() {
		return draw;
	}
	public void setDraw(long draw) {
		this.draw = draw;
	}
	public long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public long getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}

}
