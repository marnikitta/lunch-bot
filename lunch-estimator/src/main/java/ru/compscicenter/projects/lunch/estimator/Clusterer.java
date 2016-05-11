package ru.compscicenter.projects.lunch.estimator;

import ru.compscicenter.projects.lunch.model.MenuItem;

import java.util.List;
import java.util.Map;

public interface Clusterer {
    /***
     * Ставит каждому блюду его кластер в соответствие
     *
     * @param collection коллекция блюд
     * @return блюдо - номер кластера
     */
    public Map<MenuItem, Integer> doCluster(List<? extends MenuItem> sample) throws DeciderException;
}
