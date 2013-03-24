package com.myinterwebspot.app.dartnight.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * External facing api describing application data exposed by providers.
 * This is a logical representation of the data and does not dictate actual internal implementation.
 * @author hashbrown
 *
 */
public final class DataContract {

	public static final String AUTHORITY = "com.myinterwebspot.app.dartnight.data";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    
	protected interface GameColumns {
		/**
		 * The game name
		 * <P>Type: TEXT</P>
		 */
		public static final String NAME = "name";
		
		/**
		 * The game state
		 * NEW, IN_PROGRESS, COMPLETE
		 * <P>Type: TEXT</P>
		 */
		public static final String STATE = "state";
		
		/**
		 * Indicates that this was a rematch of the parent game
		 * <P>Type: INTEGER</P>
		 */
		public static final String PARENT = "parent_game";

		
		/**
		 * The timestamp for when the game was created
		 * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
		 */
		public static final String CREATED_DATE = "creation_date";

		/**
		 * The timestamp for when the game was last modified
		 * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
		 */
		public static final String MODIFIED_DATE = "modification_date";
		
		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "modification_date desc";
		
	}
	
	public static final class Games implements GameColumns, BaseColumns {
		
		private Games(){}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/games");
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + "/games/");
		
	}
}
