import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Product")
public class Product extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private HashMap<String, ProductInfo> productMap = new HashMap<>();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
//    	we retriece the author name which is stored on the web.xml file
        String authorNameQuery = request.getParameter("authorName");
        if (authorNameQuery != null && authorNameQuery.equals("info")) {
            String authorName = getServletContext().getInitParameter("authorName");
            response.setContentType("text/html");
            response.getWriter().write("<html><body>Author: " + authorName + "</body></html>");
            return;
        }

//        we retrieve the name, quntiy, and the price sent through the request
        String productName = request.getParameter("productName");
        String productQuantityStr = request.getParameter("productQuantity");
        String productPriceStr = request.getParameter("productPrice");

//          if only the product name is asked, and the other are not present or null then it just retieves the value of the key from the hashmap
        if (productName != null && productQuantityStr == null && productPriceStr == null) {
            HttpSession session = request.getSession();
            ProductInfo product = productMap.get(productName);
            if (product != null) {
                response.setContentType("text/html");
                response.getWriter().write("<html><body>Product Name: " + productName + "<br>Quantity: " + product.getQuantity() + "<br>Price: " + product.getPrice() + "</body></html>");
            } else {
                response.setContentType("text/html");
                response.getWriter().write("<html><body>Error: Product not found</body></html>");
            }
            return;
        }
// else if the others are present then it adds the item to the hashmap
        if (productQuantityStr != null && productPriceStr != null) {
            try {
                int productQuantity = Integer.parseInt(productQuantityStr);
                double productPrice = Double.parseDouble(productPriceStr);

                if (productQuantity > 1 && productQuantity < 10 && productPrice > 0) {
                    HttpSession session = request.getSession();
                    productName = request.getParameter("productName");
                    if (productName != null && productName.length() > 5 && productName.length() < 25) {
                        ProductInfo product = new ProductInfo(productQuantity, productPrice);
                        productMap.put(productName, product);
                        response.setContentType("text/html");
                        response.getWriter().write("<html><body>Product Received:<br>Name: " + productName + "<br>Quantity: " + productQuantity + "<br>Price: " + productPrice + "</body></html>");
                    } else {
//                    	this throws the error if does not meet the right requirements
                        response.setContentType("text/html");
                        response.getWriter().write("<html><body>Error: Product name length should be between 6 and 24 characters</body></html>");
                    }
                } else {
                    response.setContentType("text/html");
                    response.getWriter().write("<html><body>Error: Product quantity should be between 2 and 9 and price should be greater than 0</body></html>");
                }
            } catch (NumberFormatException e) {
                response.setContentType("text/html");
                response.getWriter().write("<html><body>Error: Invalid number format</body></html>");
            }
        } else {
            response.setContentType("text/html");
            response.getWriter().write("<html><body>Error: Missing parameters</body></html>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
// addded for the hashmap
    class ProductInfo {
        private int quantity;
        private double price;

        public ProductInfo(int quantity, double price) {
            this.quantity = quantity;
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }
    }
}
