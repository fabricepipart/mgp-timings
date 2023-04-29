package org.teknichrono.mgp.api.rest.internal;

import org.teknichrono.mgp.client.model.result.Category;
import org.teknichrono.mgp.business.service.CategoryService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
