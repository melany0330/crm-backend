package org.wms.dto.report;

import java.util.Locale;

/**
 * Enumeration of supported export formats for reports.
 */
public enum ReportExportFormat {
    CSV;

    public static ReportExportFormat fromString(String value) {
        if (value == null) {
            return CSV;
        }
        return ReportExportFormat.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }
}
