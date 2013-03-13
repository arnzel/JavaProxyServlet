package proxy.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;


@WebServlet("/postForm")
public class PostFormServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				this.getClass().getClassLoader();
				InputStream inputStream = this.getClass().getResourceAsStream("/html/postForm.html");
				StringWriter writer = new StringWriter();
				IOUtils.copy(inputStream, writer);
				response.getWriter().write(writer.toString());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().write(request.getParameter("test"));
	}
	
}
