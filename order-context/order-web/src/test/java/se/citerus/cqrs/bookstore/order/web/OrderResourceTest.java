package se.citerus.cqrs.bookstore.order.web;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.order.web.transport.CartDto;
import se.citerus.cqrs.bookstore.order.web.transport.LineItemDto;
import se.citerus.cqrs.bookstore.order.web.transport.PlaceOrderRequest;
import se.citerus.cqrs.bookstore.query.QueryService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.mockito.Mockito.mock;

public class OrderResourceTest {

  private static final String SERVICE_ADDRESS = "http://localhost:8080";
  private static final String ORDER_RESOURCE = SERVICE_ADDRESS + "/order-requests";

  private static final CommandBus commandBus = mock(CommandBus.class);
  private static final QueryService queryService = mock(QueryService.class);
  private static CartClient cartClient = mock(CartClient.class);


  @ClassRule
  public static final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new OrderResource(commandBus, cartClient))
      .build();

  @After
  public void tearDown() throws Exception {
    Mockito.reset(queryService, commandBus, cartClient);
  }


  @Test
  public void testCreateOrderRequest() {
    PlaceOrderRequest newOrderRequest = new PlaceOrderRequest();
    newOrderRequest.cart = createCart();
    createOrder(newOrderRequest);
  }

  private CartDto createCart() {
    CartDto cartDto = new CartDto();
    cartDto.cartId = UUID.randomUUID().toString();
    cartDto.totalPrice = 1200;
    cartDto.totalQuantity = 10;
    cartDto.lineItems = randomLineItems();
    return cartDto;
  }

  private List<LineItemDto> randomLineItems() {
    ArrayList<LineItemDto> lineItems = new ArrayList<>();
    LineItemDto item = new LineItemDto();
    item.bookId = UUID.randomUUID().toString();
    item.price = 120;
    item.quantity = 10;
    item.title = "Some book";
    item.totalPrice = 1200;
    lineItems.add(item);
    return lineItems;
  }

  private void createOrder(PlaceOrderRequest newOrderRequest) {
    resources.client().resource(ORDER_RESOURCE).entity(newOrderRequest, APPLICATION_JSON_TYPE).post();
  }
}
