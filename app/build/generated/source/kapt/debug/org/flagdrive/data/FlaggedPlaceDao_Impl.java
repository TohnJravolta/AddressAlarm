package org.flagdrive.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FlaggedPlaceDao_Impl implements FlaggedPlaceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FlaggedPlace> __insertionAdapterOfFlaggedPlace;

  private final EntityDeletionOrUpdateAdapter<FlaggedPlace> __deletionAdapterOfFlaggedPlace;

  private final EntityDeletionOrUpdateAdapter<FlaggedPlace> __updateAdapterOfFlaggedPlace;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public FlaggedPlaceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFlaggedPlace = new EntityInsertionAdapter<FlaggedPlace>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `FlaggedPlace` (`id`,`rawAddress`,`name`,`tags`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FlaggedPlace entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getRawAddress() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getRawAddress());
        }
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getName());
        }
        final String _tmp = Converters.INSTANCE.toCsv(entity.getTags());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
      }
    };
    this.__deletionAdapterOfFlaggedPlace = new EntityDeletionOrUpdateAdapter<FlaggedPlace>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `FlaggedPlace` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FlaggedPlace entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfFlaggedPlace = new EntityDeletionOrUpdateAdapter<FlaggedPlace>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `FlaggedPlace` SET `id` = ?,`rawAddress` = ?,`name` = ?,`tags` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FlaggedPlace entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getRawAddress() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getRawAddress());
        }
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getName());
        }
        final String _tmp = Converters.INSTANCE.toCsv(entity.getTags());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        statement.bindLong(5, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM FlaggedPlace WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final FlaggedPlace place, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFlaggedPlace.insertAndReturnId(place);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final FlaggedPlace place, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfFlaggedPlace.handle(place);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final FlaggedPlace place, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfFlaggedPlace.handle(place);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getAll(final Continuation<? super List<FlaggedPlace>> $completion) {
    final String _sql = "SELECT * FROM FlaggedPlace ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<FlaggedPlace>>() {
      @Override
      @NonNull
      public List<FlaggedPlace> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRawAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "rawAddress");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final List<FlaggedPlace> _result = new ArrayList<FlaggedPlace>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FlaggedPlace _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRawAddress;
            if (_cursor.isNull(_cursorIndexOfRawAddress)) {
              _tmpRawAddress = null;
            } else {
              _tmpRawAddress = _cursor.getString(_cursorIndexOfRawAddress);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final List<String> _tmpTags;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTags)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTags);
            }
            _tmpTags = Converters.INSTANCE.fromCsv(_tmp);
            _item = new FlaggedPlace(_tmpId,_tmpRawAddress,_tmpName,_tmpTags);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
