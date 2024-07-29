package ec.edu.uce.demo.repository;

public interface Subject {

    void registroObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObserver(String message);
}
