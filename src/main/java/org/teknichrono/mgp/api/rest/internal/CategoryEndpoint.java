package org.teknichrono.mgp.api.rest.internal;

import org.teknichrono.mgp.client.model.result.Category;
import org.teknichrono.mgp.business.service.CategoryService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/internal/category")
public class CategoryEndpoint {

  @Inject
  CategoryService categoryService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}")
  public List<Category> categoriesOfEvent(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName) {
    return categoryService.categoriesOfEvent(year, eventShortName);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/test/{year}/{eventShortName}")
  public List<Category> categoriesOfTest(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName) {
    return categoryService.categoriesOfEvent(year, eventShortName);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}")
  public Optional<Category> categoryOfEvent(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category) {
    return categoryService.categoryOfEvent(year, eventShortName, category);

  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/test/{year}/{eventShortName}/{category}")
  public Optional<Category> categoryOfTest(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category) {
    return categoryService.categoryOfEvent(year, eventShortName, category);
  }
}
