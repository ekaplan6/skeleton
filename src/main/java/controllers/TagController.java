package controllers;

import api.CreateReceiptRequest;
import api.ReceiptResponse;
import dao.ReceiptDao;
import dao.TagsDao;
import generated.tables.records.ReceiptsRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("tags")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {
    final TagsDao tag;

    public TagController(TagsDao tag) {
        this.tag = tag;
    }

    @PUT
    @Path("/{tag}")
    public void toggleTag(@PathParam("tag") String tagName, int id) {
        tag.toggle(tagName, id);
    }

    @GET
    @Path("/{tag}")
    public List<ReceiptResponse> getReceipt(@PathParam("tag") String tagName) {
        List<ReceiptsRecord> receiptRecords = tag.get(tagName);
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());

    }
}
