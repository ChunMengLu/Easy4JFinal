package net.dreamlu.easy.module.excel;

/**
 * Created by lcm on 16/4/22.
 */
public class EasyExcelInfo implements Comparable<EasyExcelInfo> {
	private final String cellName;
	private final CellType cellType;
	private final String format;
	private final int order;
	private String columnName;

	public EasyExcelInfo(ExcelCell excelCell) {
		this.cellName = excelCell.cellName();
		this.cellType = excelCell.cellType();
		this.format   = excelCell.format();
		this.order = excelCell.order();
	}

	@Override
	public int compareTo(EasyExcelInfo o) {
		int x = this.getOrder();
		int y = o.getOrder();
		return (x < y) ? -1 : ((x == y) ? 0 : 1);
	}

	public String getCellName() {
		return cellName;
	}

	public CellType getCellType() {
		return cellType;
	}

	public String getFormat() {
		return format;
	}

	public int getOrder() {
		return order;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
}
