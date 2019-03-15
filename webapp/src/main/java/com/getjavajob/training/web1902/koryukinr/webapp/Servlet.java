package com.getjavajob.training.web1902.koryukinr.webapp;

import com.getjavajob.training.web1902.koryukinr.common.Account;
import com.getjavajob.training.web1902.koryukinr.service.AccountService;
import com.getjavajob.training.web1902.koryukinr.service.exception.ServiceException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

public class Servlet extends HttpServlet {
    private AccountService accountService;
    private ListIterator<Account> listIterator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Account> accountList;
        try {
            accountService = new AccountService();
            accountList = accountService.getAllAccounts();
            listIterator = accountList.listIterator();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        StringBuilder resultString = new StringBuilder();
        resultString.append("" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Lesson16</title>\n" +
                "</head>\n" +
                "<body>" +
                "<h1>Информация об аккаунтах</h1>");
        while (listIterator.hasNext()) {
            Account account = listIterator.next();
            resultString.
                    append("\n<h2>Аккаунт ").
                    append(account.getId()).append(": ").append("</h2>\n").
                    append("<p><b>Имя:</b> ").append(account.getFirstName()).
                    append(", <b>Фамилия:</b> ").append(account.getLastName()).
                    append("</p>\n").append("<p><b>Рабочий телефон:</b> ").
                    append(account.getWorkPhone()).append(", <b>Мобильный телефон:</b> ").
                    append(account.getPersonalPhone()).append("</p>\n").append("<p><b>Домашний адрес:</b> ").
                    append(account.getHomeAddress()).append("</p>\n").append("\n");
        }
        resultString.append("" +
                "</body>\n" +
                "</html>");

        resp.getWriter().write(resultString.toString());
    }
}
