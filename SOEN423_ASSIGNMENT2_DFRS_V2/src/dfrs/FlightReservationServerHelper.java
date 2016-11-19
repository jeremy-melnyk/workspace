package dfrs;


/**
* dfrs/FlightReservationServerHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/Users/Jerem/Source/Repos/workspace/SOEN423_ASSIGNMENT2_DFRS_V2/src/FlightReservationServer.idl
* Tuesday, November 1, 2016 4:03:12 PM EDT
*/

abstract public class FlightReservationServerHelper
{
  private static String  _id = "IDL:dfrs/FlightReservationServer:1.0";

  public static void insert (org.omg.CORBA.Any a, dfrs.FlightReservationServer that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static dfrs.FlightReservationServer extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (dfrs.FlightReservationServerHelper.id (), "FlightReservationServer");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static dfrs.FlightReservationServer read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_FlightReservationServerStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, dfrs.FlightReservationServer value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static dfrs.FlightReservationServer narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof dfrs.FlightReservationServer)
      return (dfrs.FlightReservationServer)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      dfrs._FlightReservationServerStub stub = new dfrs._FlightReservationServerStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static dfrs.FlightReservationServer unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof dfrs.FlightReservationServer)
      return (dfrs.FlightReservationServer)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      dfrs._FlightReservationServerStub stub = new dfrs._FlightReservationServerStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}