/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RSA;

/**
 *
 * @author estre
 */
import java.math.BigInteger;
import java.util.*;

public final class save {

    int tamPrimo;
    private BigInteger n, q, p;
    private BigInteger totient;
    private BigInteger e, d;

    public save(int tamPrimo){
        this.tamPrimo=tamPrimo;
    }
    
    public void generarTamPrimo(){
        int contador=2;
        boolean primo=true;
        
        while((primo) && (contador!=tamPrimo)){
            if(tamPrimo % contador==0)
                primo=false;
            contador++;
        }
        this.tamPrimo=tamPrimo;
    }
    
    public void generaPrimos()
    {
        p=new BigInteger(tamPrimo,10,new Random());
        do q=new BigInteger(tamPrimo,10,new Random());
            while(q.compareTo(p)==0);
        /*int bitLength=32;
        Random rnd = new Random();
        p = BigInteger.probablePrime(bitLength, rnd);
        do q = BigInteger.probablePrime(bitLength, rnd);
            while(q.compareTo(p)==0);
        */
    }
    
    public void generaClaves()
    {
        // n = p * q
        n = p.multiply(q);
        // toltient = (p-1)*(q-1)
        totient = p.subtract(BigInteger.valueOf(1));
        totient = totient.multiply(q.subtract(BigInteger.valueOf(1)));
        // Elegimos un e coprimo de y menor que n
        
        do e=new BigInteger(2*tamPrimo, new Random());
           while((e.compareTo(totient)!= -1) || (e.gcd(totient).compareTo(BigInteger.valueOf(1))!=0));
        /*
        int bitLength=32;
        Random rnd = new Random();
        do e = BigInteger.probablePrime(bitLength, rnd);
            while((e.compareTo(totient) != -1) ||
		 (e.gcd(totient).compareTo(BigInteger.valueOf(1)) != 0));
        */
        // d = e^1 mod totient
        d = e.modInverse(totient);
    }
    
    public BigInteger[] encripta(String mensaje)
    {
        int i;
        byte[] temp = new byte[1];
        byte[] digitos = mensaje.getBytes();
        BigInteger[] bigdigitos = new BigInteger[digitos.length];

        for(i=0; i<bigdigitos.length;i++){
            temp[0] = digitos[i];
            bigdigitos[i] = new BigInteger(temp);
        }

        BigInteger[] encriptado = new BigInteger[bigdigitos.length];

        for(i=0; i<bigdigitos.length; i++)
            encriptado[i] = bigdigitos[i].modPow(e,n);

        return(encriptado);
    }
    
    public String descencripta(BigInteger[] encriptado) {
        BigInteger[] desencriptado = new BigInteger[encriptado.length];

        for(int i=0; i<desencriptado.length; i++)
            desencriptado[i] = encriptado[i].modPow(d,n);

        char[] charArray = new char[desencriptado.length];

        for(int i=0; i<charArray.length; i++)
            charArray[i] = (char) (desencriptado[i].intValue());

        return(new String(charArray));
    }
    
    public void setD(BigInteger d){
        this.d=d;
    }
    
    public void setN(BigInteger n) {
        this.n=n;
    }
    
    public BigInteger damep() {return(p);}
    public BigInteger dameq() {return(q);}
    public BigInteger dameTotient() {return(totient);}
    public BigInteger damen() {return(n);}
    public BigInteger damee() {return(e);}
    public BigInteger damed() {return(d);}

}