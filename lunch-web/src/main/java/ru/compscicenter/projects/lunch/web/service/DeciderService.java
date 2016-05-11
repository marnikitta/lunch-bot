package ru.compscicenter.projects.lunch.web.service;

import ru.compscicenter.projects.lunch.model.MenuItem;
import ru.compscicenter.projects.lunch.web.exception.NoMenuForDateException;
import ru.compscicenter.projects.lunch.web.exception.NoSuchUserException;

import java.util.Calendar;
import java.util.List;

public interface DeciderService {
    double sumForPeriod(long userId, Calendar start, Calendar end) throws NoSuchUserException;

    List<MenuItem> getForDate(long userId, Calendar date) throws NoMenuForDateException, NoSuchUserException;
}
