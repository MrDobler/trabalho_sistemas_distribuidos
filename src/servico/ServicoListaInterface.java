package servico;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import item.Item;

public interface ServicoListaInterface extends Remote {
	public void addItem(String nome, int Quant) throws RemoteException;
	public List<Item> getLista() throws RemoteException;
	public void removeItem(String nomeItemARemover) throws RemoteException;
	public void setIdCliente(long id) throws RemoteException;
	public void menu(long id) throws RemoteException;
	public void trocaVez() throws RemoteException;
	public boolean minhaVez() throws RemoteException;
	public void confirmar(long id) throws RemoteException;
	public long idConfirmou() throws RemoteException;
	public void teste() throws RemoteException;
}
