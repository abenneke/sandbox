package test;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

public class YNPrimitiveBooleanUserType implements UserType {

	@Override
	public int[] sqlTypes() {
		return new int[] { Types.CHAR };
	}

	@Override
	public Class<?> returnedClass() {
		// primitive to get the primitive type generated in the entity!
		return boolean.class; 
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return Objects.equals(x, y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return Objects.hashCode(x);
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		return "Y".equals(rs.getString(names[0]));
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		st.setString(index, ((Boolean) value).booleanValue() ? "Y" : "N");
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		throw new UnsupportedOperationException("unused");
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		throw new UnsupportedOperationException("unused");
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		throw new UnsupportedOperationException("unused");
	}

}
