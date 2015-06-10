package fr.sii.domain.spreadsheet;

/**
 * Created by tmaugin on 10/06/2015.
 * SII
 */
public class GoogleSpreadsheetCellAddress {
    public final int row;
    public final int col;
    public final String idString;

    /**
     * Constructs a CellAddress representing the specified {@code row} and
     * {@code col}.  The idString will be set in 'RnCn' notation.
     */
    public GoogleSpreadsheetCellAddress(int row, int col, String idString) {
        this.row = row;
        this.col = col;
        this.idString = idString;
    }
}