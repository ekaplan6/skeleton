package dao;

import generated.tables.Tags;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class TagsDao {
    DSLContext dsl;

    public TagsDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String merchantName, BigDecimal amount) {
        ReceiptsRecord receiptsRecord = dsl
                .insertInto(RECEIPTS, RECEIPTS.MERCHANT, RECEIPTS.AMOUNT)
                .values(merchantName, amount)
                .returning(RECEIPTS.ID)
                .fetchOne();

        checkState(receiptsRecord != null && receiptsRecord.getId() != null, "Insert failed");

        return receiptsRecord.getId();
    }

    public List<ReceiptsRecord> getAllReceipts() {
        return dsl.selectFrom(RECEIPTS).fetch();
    }

    public void toggle(String tag, int id) {
        ReceiptsRecord receiptsRecord = dsl.selectFrom(RECEIPTS).where(RECEIPTS.ID.eq(id)).fetchOne();
        if (receiptsRecord != null) {
            if (dsl.selectFrom(TAGS).where(TAGS.ID.eq(id).and(TAGS.TAG.eq(tag))).fetchOne() == null) {
                dsl.insertInto(TAGS).values(id,tag).execute();
            } else {
                dsl.deleteFrom(TAGS).where(TAGS.ID.eq(id).and(TAGS.TAG.eq(tag))).execute();
            }
        }
    }

    public List<ReceiptsRecord> get(String tagName) {
        return dsl.selectFrom(RECEIPTS).where(RECEIPTS.ID.in(dsl.select(TAGS.ID).
                from(TAGS).where(TAGS.TAG.eq(tagName)).fetch())).fetch();
    }
}
