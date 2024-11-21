package com.code.ecommercebackend.services.excel;

import org.apache.poi.ss.usermodel.*;

public class ExcelStyle {

    public static CellStyle headerStyle(Workbook wb) {
        Font whiteFont = wb.createFont();
        whiteFont.setColor(IndexedColors.WHITE.getIndex());
        whiteFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headerStyle.setFont(whiteFont);

        return headerStyle;
    }

    public static CellStyle cellStyle(Workbook wb) {
        CellStyle rowHeaderStyle = wb.createCellStyle();
        rowHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
        rowHeaderStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        rowHeaderStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        rowHeaderStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        return rowHeaderStyle;
    }
}
