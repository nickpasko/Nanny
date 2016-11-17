package com.ninja.nanny.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ninja.nanny.Model.Bank;
import com.ninja.nanny.Model.Payment;
import com.ninja.nanny.Model.Sms;
import com.ninja.nanny.Model.Transaction;
import com.ninja.nanny.Model.Wish;
import com.ninja.nanny.Model.WishSaving;
import com.ninja.nanny.Utils.Common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Logcat tag
	private static final String LOG = "DatabaseHelper";
	// Database Version
	private static final int DATABASE_VERSION = 1;
	//Database Name
	private static final String DATABASE_NAME = "Nanny";

	//table names
	private static final String TBL_BANKS = "banks";
	private static final String TBL_TRANSACTION = "transactions";
	private static final String TBL_WISH = "wish";
	private static final String TBL_WISH_SAVING = "wish_saving";
	private static final String TBL_PAYMENTS = "payments";
	private static final String TBL_SMS = "sms";

	//bank table keys
	private static final String KEY_ID = "id";
	private static final String KEY_ACCOUNT_NAME = "account_name";
	private static final String KEY_IDX_KIND = "idx_kind";
	private static final String KEY_BALANCE = "balance";
	private static final String KEY_NOTIFICATION_MODE = "notification_mode";
	private static final String KEY_FLAG_ACTIVE = "flag_active";

	//transaction table keys
	private static final String KEY_IDENTIFIER = "identifier";
	private static final String KEY_BANK_ID = "bank_id";
	private static final String KEY_SMS_ID = "sms_id";
	private static final String KEY_AMOUNT = "amount";
	private static final String KEY_MODE = "mode";
	private static final String KEY_CREATED_AT = "created_at";

	//wish table keys
	private static final String KEY_TITLE = "title";
	private static final String KEY_TOTAL_AMOUNT = "total_amount";
	private static final String KEY_MONTHLY_PAYMENT = "monthly_payment";
	private static final String KEY_SAVED_AMOUNT = "saved_amount";
	private static final String KEY_LAST_SAVING_ID = "last_saving_id";

	//wish_saving table keys
	private static final String KEY_WISH_ID = "wish_id";

	//payment table keys
	private static final String KEY_DETAIL = "detail";
	private static final String KEY_DATE_OF_MONTH = "date_of_month";
	private static final String KEY_PAYMENT_MODE = "payment_mode";
	private static final String KEY_PAID_STATUS = "paid_status";

	//sms table keys
	private static final String KEY_ADDRESS = "address";
	private static final String KEY_TEXT = "text";
	private static final String KEY_TIME_STAMP = "time_stamp";

	// Banks table create statement
	private static final String CREATE_TABLE_BANKS = "CREATE TABLE IF NOT EXISTS "
			+ TBL_BANKS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ACCOUNT_NAME
			+ " TEXT," + KEY_IDX_KIND + " INTEGER," + KEY_BALANCE + " INTEGER,"
			 + KEY_NOTIFICATION_MODE + " INTEGER," + KEY_FLAG_ACTIVE + " INTEGER" + ")";

	// Transactions table create statement
	private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE IF NOT EXISTS "
			+ TBL_TRANSACTION + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ACCOUNT_NAME
			+ " TEXT," + KEY_IDENTIFIER + " TEXT," + KEY_BANK_ID + " INTEGER," + KEY_SMS_ID + " INTEGER,"
			+ KEY_AMOUNT + " INTEGER," + KEY_MODE + " INTEGER,"	+ KEY_CREATED_AT + " INTEGER" + ")";

	// Wish table create statement
	private static final String CREATE_TABLE_WISH = "CREATE TABLE IF NOT EXISTS "
			+ TBL_WISH + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE
			+ " TEXT," + KEY_TOTAL_AMOUNT + " INTEGER," + KEY_MONTHLY_PAYMENT + " INTEGER,"
			+ KEY_SAVED_AMOUNT + " INTEGER," + KEY_CREATED_AT + " INTEGER," + KEY_LAST_SAVING_ID + " INTEGER," + KEY_FLAG_ACTIVE + " INTEGER" + ")";

	// WishSaving table create statement
	private static final String CREATE_TABLE_WISH_SAVING = "CREATE TABLE IF NOT EXISTS "
			+ TBL_WISH_SAVING + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_WISH_ID
			+ " INTEGER," + KEY_SAVED_AMOUNT + " INTEGER," + KEY_CREATED_AT + " INTEGER" + ")";

	// Payments table create statement
	private static final String CREATE_TABLE_PAYMENTS = "CREATE TABLE IF NOT EXISTS "
			+ TBL_PAYMENTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_DETAIL
			+ " TEXT," + KEY_AMOUNT + " INTEGER," + KEY_DATE_OF_MONTH + " INTEGER,"
			+ KEY_PAYMENT_MODE + " INTEGER," + KEY_PAID_STATUS + " INTEGER," + KEY_CREATED_AT + " INTEGER" + ")";

	//Sms table create statement
	private static final String CREATE_TABLE_SMS = "CREATE TABLE IF NOT EXISTS "
			+ TBL_SMS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ADDRESS
			+ " TEXT," + KEY_TEXT + " TEXT," + KEY_TIME_STAMP + " INTEGER" + ")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		db.execSQL(CREATE_TABLE_BANKS);
		db.execSQL(CREATE_TABLE_TRANSACTIONS);
		db.execSQL(CREATE_TABLE_WISH);
		db.execSQL(CREATE_TABLE_WISH_SAVING);
		db.execSQL(CREATE_TABLE_PAYMENTS);
		db.execSQL(CREATE_TABLE_SMS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TBL_BANKS);
		db.execSQL("DROP TABLE IF EXISTS " + TBL_TRANSACTION);
		db.execSQL("DROP TABLE IF EXISTS " + TBL_WISH);
		db.execSQL("DROP TABLE IF EXISTS " + TBL_PAYMENTS);
		db.execSQL("DROP TABLE IF EXISTS" + TBL_SMS);
		// create new tables
		onCreate(db);
	}

	// ------------------------ "banks" table methods ----------------//

	/*
	 * Creating a banks
	 */
	public int createBank(Bank bank) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ACCOUNT_NAME, bank.getAccountName());
		values.put(KEY_IDX_KIND, bank.getIdxKind());
		values.put(KEY_BALANCE, bank.getBalance());
		values.put(KEY_NOTIFICATION_MODE, bank.getNotificationMode());
		values.put(KEY_FLAG_ACTIVE, bank.getFlagActive());

		// insert row
		long bank_id = db.insert(TBL_BANKS, null, values);


		return (int)bank_id;
	}

	/*
	 * get single bank
	 */
	public Bank getBank(int bank_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TBL_BANKS + " WHERE "
				+ KEY_ID + " = " + bank_id;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Bank bank = new Bank();
		bank.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		bank.setAccountName(c.getString(c.getColumnIndex(KEY_ACCOUNT_NAME)));
		bank.setIdxKind(c.getInt(c.getColumnIndex(KEY_IDX_KIND)));
		bank.setBalance(c.getInt(c.getColumnIndex(KEY_BALANCE)));
		bank.setNotificationMode(c.getInt(c.getColumnIndex(KEY_NOTIFICATION_MODE)));
		bank.setFlagActive(c.getInt(c.getColumnIndex(KEY_FLAG_ACTIVE)));

		return bank;
	}

	/**
	 * getting all banks
	 * */
	public List<Bank> getAllBanks() {
		List<Bank> banks = new ArrayList<Bank>();
		String selectQuery = "SELECT  * FROM " + TBL_BANKS;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Bank bank = new Bank();
				bank.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				bank.setAccountName(c.getString(c.getColumnIndex(KEY_ACCOUNT_NAME)));
				bank.setIdxKind(c.getInt(c.getColumnIndex(KEY_IDX_KIND)));
				bank.setBalance(c.getInt(c.getColumnIndex(KEY_BALANCE)));
				bank.setNotificationMode(c.getInt(c.getColumnIndex(KEY_NOTIFICATION_MODE)));
				bank.setFlagActive(c.getInt(c.getColumnIndex(KEY_FLAG_ACTIVE)));

				// adding to bank list
				banks.add(bank);
			} while (c.moveToNext());
		}

		return banks;
	}

	/*
	 * getting bank count
	 */
	public int getBankCount() {
		String countQuery = "SELECT  * FROM " + TBL_BANKS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/*
	 * Updating a bank
	 */
	public int updateBank(Bank bank) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ACCOUNT_NAME, bank.getAccountName());
		values.put(KEY_IDX_KIND, bank.getIdxKind());
		values.put(KEY_BALANCE, bank.getBalance());
		values.put(KEY_NOTIFICATION_MODE, bank.getNotificationMode());
		values.put(KEY_FLAG_ACTIVE, bank.getFlagActive());

		// updating row
		return db.update(TBL_BANKS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(bank.getId()) });
	}

	/*
	 * Deleting a bank
	 */
	public void deleteBank(int bank_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TBL_BANKS, KEY_ID + " = ?",
				new String[] { String.valueOf(bank_id) });
	}

	// ------------------------ "wish" table methods ----------------//
	/*
	 * Creating a wish
	 */

	public int createWish(Wish wish) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, wish.getTitle());
		values.put(KEY_TOTAL_AMOUNT, wish.getTotalAmount());
		values.put(KEY_MONTHLY_PAYMENT, wish.getMonthlyPayment());
		values.put(KEY_SAVED_AMOUNT, wish.getSavedAmount());
		values.put(KEY_CREATED_AT, wish.getTimestampCreated());
		values.put(KEY_LAST_SAVING_ID, wish.getLastSavingId());
		values.put(KEY_FLAG_ACTIVE, wish.getFlagActive());

		// insert row
		long wish_id = db.insert(TBL_WISH, null, values);

		return (int)wish_id;
	}

	/*
	 * get single wish
	 */
	public Wish getWish(int wish_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TBL_WISH + " WHERE "
				+ KEY_ID + " = " + wish_id;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Wish wish = new Wish();
		wish.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		wish.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
		wish.setTotalAmount(c.getInt(c.getColumnIndex(KEY_TOTAL_AMOUNT)));
		wish.setMonthlyPayment(c.getInt(c.getColumnIndex(KEY_MONTHLY_PAYMENT)));
		wish.setSavedAmount(c.getInt(c.getColumnIndex(KEY_SAVED_AMOUNT)));
		wish.setTimestampCreated(c.getInt(c.getColumnIndex(KEY_CREATED_AT)));
		wish.setLastSavingId(c.getInt(c.getColumnIndex(KEY_LAST_SAVING_ID)));
		wish.setFlagActive(c.getInt(c.getColumnIndex(KEY_FLAG_ACTIVE)));

		return wish;
	}

	/**
	 * getting all wishes
	 * */
	public List<Wish> getAllWishes() {
		List<Wish> wishes = new ArrayList<Wish>();
		String selectQuery = "SELECT  * FROM " + TBL_WISH;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Wish wish = new Wish();
				wish.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				wish.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
				wish.setTotalAmount(c.getInt(c.getColumnIndex(KEY_TOTAL_AMOUNT)));
				wish.setMonthlyPayment(c.getInt(c.getColumnIndex(KEY_MONTHLY_PAYMENT)));
				wish.setSavedAmount(c.getInt(c.getColumnIndex(KEY_SAVED_AMOUNT)));
				wish.setTimestampCreated(c.getLong(c.getColumnIndex(KEY_CREATED_AT)));
				wish.setLastSavingId(c.getInt(c.getColumnIndex(KEY_LAST_SAVING_ID)));
				wish.setFlagActive(c.getInt(c.getColumnIndex(KEY_FLAG_ACTIVE)));

				// adding to transaction list
				wishes.add(wish);
			} while (c.moveToNext());
		}

		return wishes;
	}

	/**
	 * getting active wishes
	 * */
	public List<Wish> getActiveWishes() {
		List<Wish> wishes = new ArrayList<Wish>();
		String selectQuery = "SELECT  * FROM " + TBL_WISH + " WHERE " + KEY_FLAG_ACTIVE + "=1";

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Wish wish = new Wish();
				wish.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				wish.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
				wish.setTotalAmount(c.getInt(c.getColumnIndex(KEY_TOTAL_AMOUNT)));
				wish.setMonthlyPayment(c.getInt(c.getColumnIndex(KEY_MONTHLY_PAYMENT)));
				wish.setSavedAmount(c.getInt(c.getColumnIndex(KEY_SAVED_AMOUNT)));
				wish.setTimestampCreated(c.getLong(c.getColumnIndex(KEY_CREATED_AT)));
				wish.setLastSavingId(c.getInt(c.getColumnIndex(KEY_LAST_SAVING_ID)));
				wish.setFlagActive(c.getInt(c.getColumnIndex(KEY_FLAG_ACTIVE)));

				// adding to transaction list
				wishes.add(wish);
			} while (c.moveToNext());
		}

		return wishes;
	}

	/**
	 * getting active wishes
	 * */
	public List<Wish> getFinishedWishes() {
		List<Wish> wishes = new ArrayList<Wish>();
		String selectQuery = "SELECT  * FROM " + TBL_WISH + " WHERE " + KEY_FLAG_ACTIVE + "=0";

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Wish wish = new Wish();
				wish.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				wish.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
				wish.setTotalAmount(c.getInt(c.getColumnIndex(KEY_TOTAL_AMOUNT)));
				wish.setMonthlyPayment(c.getInt(c.getColumnIndex(KEY_MONTHLY_PAYMENT)));
				wish.setSavedAmount(c.getInt(c.getColumnIndex(KEY_SAVED_AMOUNT)));
				wish.setTimestampCreated(c.getLong(c.getColumnIndex(KEY_CREATED_AT)));
				wish.setLastSavingId(c.getInt(c.getColumnIndex(KEY_LAST_SAVING_ID)));
				wish.setFlagActive(c.getInt(c.getColumnIndex(KEY_FLAG_ACTIVE)));

				// adding to transaction list
				wishes.add(wish);
			} while (c.moveToNext());
		}

		return wishes;
	}

	/*
	 * getting wish count
	 */
	public int getWishesCount() {
		String countQuery = "SELECT  * FROM " + TBL_WISH;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/*
	 * Updating a wish
	 */
	public int updateWish(Wish wish) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, wish.getTitle());
		values.put(KEY_TOTAL_AMOUNT, wish.getTotalAmount());
		values.put(KEY_MONTHLY_PAYMENT, wish.getMonthlyPayment());
		values.put(KEY_SAVED_AMOUNT, wish.getSavedAmount());
		values.put(KEY_CREATED_AT, wish.getTimestampCreated());
		values.put(KEY_LAST_SAVING_ID, wish.getLastSavingId());
		values.put(KEY_FLAG_ACTIVE, wish.getFlagActive());

		// updating row
		return db.update(TBL_WISH, values, KEY_ID + " = ?",
				new String[] { String.valueOf(wish.getId()) });
	}

	/*
	 * Deleting a wish
	 */
	public void deleteWish(int wish_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TBL_WISH, KEY_ID + " = ?",
				new String[] { String.valueOf(wish_id) });
	}

	// ------------------------ "wish_saving" table methods ----------------//
	/*
	 * Creating a wishSaving
	 */

	public int createWishSaving(WishSaving wishSaving) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_WISH_ID, wishSaving.getWishId());
		values.put(KEY_SAVED_AMOUNT, wishSaving.getSavedAmount());
		values.put(KEY_CREATED_AT, wishSaving.getDateCreated());

		// insert row
		long wishSaving_id = db.insert(TBL_WISH_SAVING, null, values);

		return (int)wishSaving_id;
	}

	/*
	 * get single wishSaving with id
	 */
	public WishSaving getWishSaving(int wishSaving_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TBL_WISH_SAVING + " WHERE "
				+ KEY_ID + " = " + wishSaving_id;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		WishSaving wishSaving = new WishSaving();
		wishSaving.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		wishSaving.setWishId(c.getInt(c.getColumnIndex(KEY_WISH_ID)));
		wishSaving.setSavedAmount(c.getInt(c.getColumnIndex(KEY_SAVED_AMOUNT)));
		wishSaving.setDateCreated(c.getInt(c.getColumnIndex(KEY_CREATED_AT)));

		return wishSaving;
	}

	/**
	 * getting all wishSavings for specific wishSaving_id;
	 * */
	public List<WishSaving> getAllWishSavings(int wish_id) {
		List<WishSaving> wishSavings = new ArrayList<WishSaving>();
		String selectQuery = "SELECT  * FROM " + TBL_WISH_SAVING + " WHERE " + KEY_WISH_ID + "=" + wish_id;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				WishSaving wishSaving = new WishSaving();
				wishSaving.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				wishSaving.setWishId(c.getInt(c.getColumnIndex(KEY_WISH_ID)));
				wishSaving.setSavedAmount(c.getInt(c.getColumnIndex(KEY_SAVED_AMOUNT)));
				wishSaving.setDateCreated(c.getInt(c.getColumnIndex(KEY_CREATED_AT)));

				// adding to transaction list
				wishSavings.add(wishSaving);
			} while (c.moveToNext());
		}

		return wishSavings;
	}

	/*
	 * Updating a wishSaving
	 */
	public int updateWishSaving(WishSaving wishSaving) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_WISH_ID, wishSaving.getWishId());
		values.put(KEY_SAVED_AMOUNT, wishSaving.getSavedAmount());
		values.put(KEY_CREATED_AT, wishSaving.getDateCreated());

		// updating row
		return db.update(TBL_WISH_SAVING, values, KEY_ID + " = ?",
				new String[] { String.valueOf(wishSaving.getId()) });
	}

	/*
	 * Deleting a wishSaving with wish id
	 */
	public void deleteWishSavingGroup(int wish_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TBL_WISH_SAVING, KEY_WISH_ID + " = ?",
				new String[] { String.valueOf(wish_id) });
	}

	/*
	 * Deleting a wishSaving with wishSaving id
	 */
	public void deleteWishSaving(int wishSaving_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TBL_WISH_SAVING, KEY_ID + " = ?",
				new String[] { String.valueOf(wishSaving_id) });
	}



	// ------------------------ "transactions" table methods ----------------//

	/*
	 * Creating a transactions
	 */
	public int createTransaction(Transaction transaction) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ACCOUNT_NAME, transaction.getAccountName());
		values.put(KEY_IDENTIFIER, transaction.getIdentifier());
		values.put(KEY_BANK_ID, transaction.getBankId());
		values.put(KEY_SMS_ID, transaction.getSmsId());
		values.put(KEY_AMOUNT, transaction.getAmount());
		values.put(KEY_MODE, transaction.getMode());
		values.put(KEY_CREATED_AT, transaction.getTimestampCreated());

		// insert row
		long transaction_id = db.insert(TBL_TRANSACTION, null, values);

		return (int)transaction_id;
	}

	/*
	 * get single transaction
	 */
	public Transaction getTransaction(int transaction_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TBL_TRANSACTION + " WHERE "
				+ KEY_ID + " = " + transaction_id;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Transaction transaction = new Transaction();
		transaction.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		transaction.setAccountName(c.getString(c.getColumnIndex(KEY_ACCOUNT_NAME)));
		transaction.setIdentifier(c.getString(c.getColumnIndex(KEY_IDENTIFIER)));
		transaction.setBankId(c.getInt(c.getColumnIndex(KEY_BANK_ID)));
		transaction.setSmsId(c.getInt(c.getColumnIndex(KEY_SMS_ID)));
		transaction.setAmount(c.getInt(c.getColumnIndex(KEY_AMOUNT)));
		transaction.setMode(c.getInt(c.getColumnIndex(KEY_MODE)));
		transaction.setTimestampCreated(c.getLong(c.getColumnIndex(KEY_CREATED_AT)));

		return transaction;
	}

	/**
	 * getting all transactions
	 * */
	public List<Transaction> getAllTransactions() {
		List<Transaction> transactions = new ArrayList<Transaction>();
		String selectQuery = "SELECT  * FROM " + TBL_TRANSACTION;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Transaction transaction = new Transaction();
				transaction.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				transaction.setAccountName(c.getString(c.getColumnIndex(KEY_ACCOUNT_NAME)));
				transaction.setIdentifier(c.getString(c.getColumnIndex(KEY_IDENTIFIER)));
				transaction.setBankId(c.getInt(c.getColumnIndex(KEY_BANK_ID)));
				transaction.setSmsId(c.getInt(c.getColumnIndex(KEY_SMS_ID)));
				transaction.setAmount(c.getInt(c.getColumnIndex(KEY_AMOUNT)));
				transaction.setMode(c.getInt(c.getColumnIndex(KEY_MODE)));
				transaction.setTimestampCreated(c.getLong(c.getColumnIndex(KEY_CREATED_AT)));

				// adding to transaction list
				transactions.add(transaction);
			} while (c.moveToNext());
		}

		return transactions;
	}

	/*
	 * getting transaction count
	 */
	public int getTransactionCount() {
		String countQuery = "SELECT  * FROM " + TBL_TRANSACTION;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/*
	 * Updating a transaction
	 */
	public int updateTransaction(Transaction transaction) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ACCOUNT_NAME, transaction.getAccountName());
		values.put(KEY_IDENTIFIER, transaction.getIdentifier());
		values.put(KEY_BANK_ID, transaction.getBankId());
		values.put(KEY_SMS_ID, transaction.getSmsId());
		values.put(KEY_AMOUNT, transaction.getAmount());
		values.put(KEY_MODE, transaction.getMode());
		values.put(KEY_CREATED_AT, transaction.getTimestampCreated());

		// updating row
		return db.update(TBL_TRANSACTION, values, KEY_ID + " = ?",
				new String[] { String.valueOf(transaction.getId()) });
	}

	/*
	 * Deleting a transaction
	 */
	public void deleteTransaction(int transaction_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TBL_TRANSACTION, KEY_ID + " = ?",
				new String[] { String.valueOf(transaction_id) });
	}

	// ------------------------ "sms" table methods ----------------//

	/*
	 * Creating a sms
	 */
	public int createSMS(Sms sms) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ADDRESS, sms.getAddress());
		values.put(KEY_TEXT, sms.getText());
		values.put(KEY_TIME_STAMP, sms.getTimestamp());

		// insert row
		long sms_id = db.insert(TBL_SMS, null, values);

		return (int)sms_id;
	}
	/*
	 * get single sms
	 */
	public Sms getSms(int sms_id) {
		if(Common.getInstance().listSms.size() > 0) {
			return Common.getInstance().listSms.get(sms_id - 1);
		}

		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TBL_SMS + " WHERE "
				+ KEY_ID + " = " + sms_id;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Sms sms = new Sms();
		sms.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		sms.setAddress(c.getString(c.getColumnIndex(KEY_ADDRESS)));
		sms.setText(c.getString(c.getColumnIndex(KEY_TEXT)));
		sms.setTimestamp(c.getLong(c.getColumnIndex(KEY_TIME_STAMP)));

		return sms;
	}

	/**
	 * getting all smses
	 * */
	public List<Sms> getAllSms() {
		List<Sms> smses = new ArrayList<Sms>();
		String selectQuery = "SELECT  * FROM " + TBL_SMS;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Sms sms = new Sms();
				sms.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				sms.setAddress(c.getString(c.getColumnIndex(KEY_ADDRESS)));
				sms.setText(c.getString(c.getColumnIndex(KEY_TEXT)));
				sms.setTimestamp(c.getLong(c.getColumnIndex(KEY_TIME_STAMP)));

				// adding to payment list
				smses.add(sms);
			} while (c.moveToNext());
		}

		return smses;
	}

	/*
	 * getting sms count
	 */
	public int getSmsCount() {
		String countQuery = "SELECT  * FROM " + TBL_SMS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/*
	 * Updating a sms
	 */
	public int updateSms(Sms sms) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_ADDRESS, sms.getAddress());
		values.put(KEY_TEXT, sms.getText());
		values.put(KEY_TIME_STAMP, sms.getTimestamp());

		// updating row
		return db.update(TBL_SMS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(sms.getId()) });
	}

	/*
	 * Deleting a sms
	 */
	public void deleteSms(int sms_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TBL_SMS, KEY_ID + " = ?",
				new String[] { String.valueOf(sms_id) });
	}

	// ------------------------ "payments" table methods ----------------//

	/*
	 * Creating a payment
	 */
	public int createPayment(Payment payment) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, payment.getTitle());
		values.put(KEY_DETAIL, payment.getDetail());
		values.put(KEY_AMOUNT, payment.getAmount());
		values.put(KEY_DATE_OF_MONTH, payment.getDateOfMonth());
		values.put(KEY_PAYMENT_MODE, payment.getPaymentMode());
		values.put(KEY_PAID_STATUS, payment.getPaidStatus());
		values.put(KEY_CREATED_AT, payment.getTimestampCreated());

		// insert row
		long payment_id = db.insert(TBL_PAYMENTS, null, values);
		return (int)payment_id;
	}

	/*
	 * get single payment
	 */
	public Payment getPayment(int payment_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TBL_PAYMENTS + " WHERE "
				+ KEY_ID + " = " + payment_id;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Payment payment = new Payment();
		payment.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		payment.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
		payment.setDetail(c.getString(c.getColumnIndex(KEY_DETAIL)));
		payment.setAmount(c.getInt(c.getColumnIndex(KEY_AMOUNT)));
		payment.setDateOfMonth(c.getInt(c.getColumnIndex(KEY_DATE_OF_MONTH)));
		payment.setPaymentMode(c.getInt(c.getColumnIndex(KEY_PAYMENT_MODE)));
		payment.setPaidStatus(c.getInt(c.getColumnIndex(KEY_PAID_STATUS)));
		payment.setTimestampCreated(c.getLong(c.getColumnIndex(KEY_CREATED_AT)));

		return payment;
	}

	/**
	 * getting all payments
	 * */
	public List<Payment> getAllPayments() {
		List<Payment> payments = new ArrayList<Payment>();
		String selectQuery = "SELECT  * FROM " + TBL_PAYMENTS;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Payment payment = new Payment();
				payment.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				payment.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
				payment.setDetail(c.getString(c.getColumnIndex(KEY_DETAIL)));
				payment.setAmount(c.getInt(c.getColumnIndex(KEY_AMOUNT)));
				payment.setDateOfMonth(c.getInt(c.getColumnIndex(KEY_DATE_OF_MONTH)));
				payment.setPaymentMode(c.getInt(c.getColumnIndex(KEY_PAYMENT_MODE)));
				payment.setPaidStatus(c.getInt(c.getColumnIndex(KEY_PAID_STATUS)));
				payment.setTimestampCreated(c.getLong(c.getColumnIndex(KEY_CREATED_AT)));
				// adding to payment list
				payments.add(payment);
			} while (c.moveToNext());
		}

		return payments;
	}

	/*
	 * getting payment count
	 */
	public int getPaymentCount() {
		String countQuery = "SELECT  * FROM " + TBL_PAYMENTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/*
	 * Updating a payment
	 */
	public int updatePayment(Payment payment) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_TITLE, payment.getTitle());
		values.put(KEY_DETAIL, payment.getDetail());
		values.put(KEY_AMOUNT, payment.getAmount());
		values.put(KEY_DATE_OF_MONTH, payment.getDateOfMonth());
		values.put(KEY_PAYMENT_MODE, payment.getPaymentMode());
		values.put(KEY_PAID_STATUS, payment.getPaidStatus());
		values.put(KEY_CREATED_AT, payment.getTimestampCreated());

		// updating row
		return db.update(TBL_PAYMENTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(payment.getId()) });
	}

	/*
	 * Deleting a payment
	 */
	public void deletePayment(int payment_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TBL_PAYMENTS, KEY_ID + " = ?",
				new String[] { String.valueOf(payment_id) });
	}

	/**
	 * get datetime
	 * */
	public String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}


}