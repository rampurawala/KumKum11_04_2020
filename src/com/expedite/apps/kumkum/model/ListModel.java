package com.expedite.apps.kumkum.model;

public class ListModel {

	private  String Messages="";
    private  String Image="";
    private  String Url="";
     
    /*********** Set Methods ******************/
     
    public void setMesages(String Messages)
    {
        this.Messages = Messages;
    }
     
    public void setImage(String Image)
    {
        this.Image = Image;
    }
     
    public void setUrl(String Url)
    {
        this.Url = Url;
    }
     
    /*********** Get Methods ****************/
     
    public String getMessges()
    {
        return this.Messages;
    }
     
    public String getImage()
    {
        return this.Image;
    }
 
    public String getUrl()
    {
        return this.Url;
    }  
	
}
