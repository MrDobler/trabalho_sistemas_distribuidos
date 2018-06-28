package servico;

import java.rmi.Remote;
import java.rmi.RemoteException;
import shared.Item;
import shared.Status;

public interface ServicoListaInterface extends Remote {
	public void addItem(String nome, int Quant) throws RemoteException;
	public Iterable<Item> getLista() throws RemoteException;
	public void setLista(Iterable<Item> lista) throws RemoteException; 
	public void removeItem(String nomeItemARemover) throws RemoteException;
	public void setIdCliente(long id) throws RemoteException;
	public void menu(long id) throws RemoteException;
	public void confirmar(long id) throws RemoteException;
	public long idConfirmou() throws RemoteException;
	public boolean checkConfirmacao() throws RemoteException;
//	public void teste() throws RemoteException;
	
	public void mudarTurno(long id) throws RemoteException;
	public boolean checkMeuTurno(long id) throws RemoteException;
	public long getIDTurno() throws RemoteException;
	
	public Status getStatus() throws RemoteException;
}
