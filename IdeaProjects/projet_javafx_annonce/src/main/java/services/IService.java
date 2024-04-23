package services;

import java.util.List;

public interface IService<T>{
    public void addAnnonce(T t);
    public void removeAnnonce(T t);
    public void updateAnnonce(T t);
    public List<T> allData();
}