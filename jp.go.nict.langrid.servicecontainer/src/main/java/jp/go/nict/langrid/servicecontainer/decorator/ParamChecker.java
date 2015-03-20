/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.servicecontainer.decorator;

import java.security.InvalidParameterException;

/**
 * 
 * 
 */
public class ParamChecker
{
    private String command ;
    private int param ;

    /**
     * 
     * 
     */
    public String getCommand( ) { return command ; }
    public void setCommand( String command )
    {
        this.command = command ;
    }

    /**
     * 
     * 
     */
    public int getParam( ) { return param ; }
    public void setParam( int param )
    {
        this.param = param ;
    }

    /**
     * 
     * 
     */
    public void check( Object[ ] args )
    throws InvalidParameterException
    {
        if( command.compareTo( "NumCheck" ) == 0 ) {
            numCheck( args ) ;
        } else if( command.compareTo( "NullCheck" ) == 0 ) {
            nullCheck( args ) ;
        } else if( command.compareTo( "StringCheck" ) == 0 ) {
            stringCheck( args ) ;
        }
    }

    /**
     * 
     * 
     */
    public void stringCheck( Object[ ] args )
    throws InvalidParameterException
    {
        if( args.length < param - 1 ) {
            throw new InvalidParameterException( "Parameter number error." ) ;
        }
        if( args[ param ] == null ) {
            throw new InvalidParameterException( "The parameter is null." ) ;
        }
        if( args[ param ] instanceof String == false ) {
            throw new InvalidParameterException( "The parameter type is not String." ) ;
        }
    }

    /**
     * 
     * 
     */
    public void nullCheck( Object[ ] args )
    throws InvalidParameterException
    {
        if( args.length < param - 1 ) {
            throw new InvalidParameterException( "Parameter number error." ) ;
        }
        if( args[ param ] == null ) {
            throw new InvalidParameterException( "The parameter is null." ) ;
        }
    }

    /**
     * 
     * 
     */
    public void numCheck( Object[ ] args )
    throws InvalidParameterException
    {
        if( args.length != param ) {
            throw new InvalidParameterException( "Parameter number error." ) ;
        }
    }
}
