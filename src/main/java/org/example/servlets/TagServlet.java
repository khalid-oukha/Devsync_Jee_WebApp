package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entities.Tag;
import org.example.services.TagService;

import java.io.IOException;
import java.util.List;

@WebServlet("/tags")
public class TagServlet extends HttpServlet {
    private final TagService tagService = new TagService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tagId = req.getParameter("editTagId");

        if (tagId != null) {
            Tag tag = tagService.findById(Long.parseLong(tagId));
            req.setAttribute("tagToEdit", tag);
            req.getRequestDispatcher("/editTag.jsp").forward(req, resp);
        } else {
            List<Tag> tags = tagService.findAll();
            req.setAttribute("tags", tags);
            req.getRequestDispatcher("/tags.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            String tagId = req.getParameter("tagId");
            tagService.delete(Long.parseLong(tagId));
            resp.sendRedirect("tags");
        } else if ("update".equals(action)) {
            String tagId = req.getParameter("tagId");
            String tagName = req.getParameter("name");
            Tag tag = new Tag();
            tag.setId(Long.parseLong(tagId));
            tag.setName(tagName);
            tagService.update(tag);
            resp.sendRedirect("tags");
        } else if ("create".equals(action)) {
            String tagName = req.getParameter("name");
            Tag tag = new Tag();
            tag.setName(tagName);
            tagService.create(tag);
            resp.sendRedirect("tags");
        }
    }
}
