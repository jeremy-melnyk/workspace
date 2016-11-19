package dfrs;


/**
* dfrs/_FlightReservationServerStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/Users/Jerem/Source/Repos/workspace/SOEN423_ASSIGNMENT2_DFRS_V2/src/FlightReservationServer.idl
* Tuesday, November 1, 2016 4:03:12 PM EDT
*/

public class _FlightReservationServerStub extends org.omg.CORBA.portable.ObjectImpl implements dfrs.FlightReservationServer
{

  public boolean bookFlight (String firstName, String lastName, String address, String phoneNumber, String flightIdAndFlightClass)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("bookFlight", true);
                $out.write_string (firstName);
                $out.write_string (lastName);
                $out.write_string (address);
                $out.write_string (phoneNumber);
                $out.write_string (flightIdAndFlightClass);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return bookFlight (firstName, lastName, address, phoneNumber, flightIdAndFlightClass        );
            } finally {
                _releaseReply ($in);
            }
  } // bookFlight

  public String getBookedFlightCount (String managerIdAndFlightClass)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getBookedFlightCount", true);
                $out.write_string (managerIdAndFlightClass);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getBookedFlightCount (managerIdAndFlightClass        );
            } finally {
                _releaseReply ($in);
            }
  } // getBookedFlightCount

  public boolean editFlightRecord (String managerIdAndRecordIdAndDbOperation, String flightParameter, String newValue)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("editFlightRecord", true);
                $out.write_string (managerIdAndRecordIdAndDbOperation);
                $out.write_string (flightParameter);
                $out.write_string (newValue);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return editFlightRecord (managerIdAndRecordIdAndDbOperation, flightParameter, newValue        );
            } finally {
                _releaseReply ($in);
            }
  } // editFlightRecord

  public boolean transferReservation (String passengerRecordId, String currentCity, String otherCity)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("transferReservation", true);
                $out.write_string (passengerRecordId);
                $out.write_string (currentCity);
                $out.write_string (otherCity);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return transferReservation (passengerRecordId, currentCity, otherCity        );
            } finally {
                _releaseReply ($in);
            }
  } // transferReservation

  public String[] getFlights ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getFlights", true);
                $in = _invoke ($out);
                String $result[] = dfrs.StringsHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getFlights (        );
            } finally {
                _releaseReply ($in);
            }
  } // getFlights

  public String[] getAvailableFlights ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getAvailableFlights", true);
                $in = _invoke ($out);
                String $result[] = dfrs.StringsHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getAvailableFlights (        );
            } finally {
                _releaseReply ($in);
            }
  } // getAvailableFlights

  public String[] getManagerIds ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getManagerIds", true);
                $in = _invoke ($out);
                String $result[] = dfrs.StringsHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getManagerIds (        );
            } finally {
                _releaseReply ($in);
            }
  } // getManagerIds

  public String[] getPassengerRecords ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getPassengerRecords", true);
                $in = _invoke ($out);
                String $result[] = dfrs.StringsHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getPassengerRecords (        );
            } finally {
                _releaseReply ($in);
            }
  } // getPassengerRecords

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:dfrs/FlightReservationServer:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _FlightReservationServerStub
