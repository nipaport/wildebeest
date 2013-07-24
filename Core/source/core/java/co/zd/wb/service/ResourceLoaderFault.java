package co.zd.wb.service;

public class ResourceLoaderFault extends RuntimeException
{
	public ResourceLoaderFault(Exception cause)
	{
		super(cause);
	}
	
	public ResourceLoaderFault(String message)
	{
		super(message);
	}
}