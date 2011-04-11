package com.myinterwebspot.app.dartnight.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UpgradeDBToVersion3 implements DBUpgradeStrategy {


	public void upgrade(SQLiteDatabase db) {
		StringBuffer ddlBuilder = new StringBuffer();
		ddlBuilder.append("ALTER TABLE ");
		ddlBuilder.append(StatsRollupTable.TABLE_NAME);
		ddlBuilder.append(" ADD COLUMN ");
		ddlBuilder.append(StatsRollupTable.TOTAL_SCORE);
		ddlBuilder.append(" REAL");

		db.execSQL(ddlBuilder.toString());

		populateTotals(db);
	}

	private void populateTotals(SQLiteDatabase db) {

		String updateWhereClause = StatsRollupTable.ID + "= ? AND " + StatsRollupTable.TYPE + "= ?";
		String[] updateWhereArgs = new String[2];
		ContentValues updateValues = new ContentValues();

		Cursor statCurs = 
			db.query(StatsRollupTable.TABLE_NAME, null, null, null, null, null, null);

		statCurs.moveToFirst();
		while(!statCurs.isAfterLast()){
			double avgScore = statCurs.getDouble(statCurs.getColumnIndex(StatsRollupTable.AVG_SCORE));
			int wins = statCurs.getInt(statCurs.getColumnIndex(StatsRollupTable.WINS));
			double totalScore = avgScore * wins;
			updateValues.put(StatsRollupTable.TOTAL_SCORE, totalScore);
			updateWhereArgs[0] = statCurs.getString(statCurs.getColumnIndex(StatsRollupTable.ID));
			updateWhereArgs[1] = statCurs.getString(statCurs.getColumnIndex(StatsRollupTable.TYPE));

			db.update(StatsRollupTable.TABLE_NAME, updateValues, updateWhereClause, updateWhereArgs);
			statCurs.moveToNext();
		}

		statCurs.close();

	}


}
