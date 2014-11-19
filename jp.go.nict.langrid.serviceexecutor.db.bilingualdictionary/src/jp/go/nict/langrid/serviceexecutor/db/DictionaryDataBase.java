/*
 * $Id: DictionaryDataBase.java 240 2010-10-03 01:39:10Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
 * Copyright (C) 2008-2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.serviceexecutor.db;

import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.COMPLETE;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.PARTIAL;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.PREFIX;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.REGEX;
import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.SUFFIX;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.adjacencypair.AdjacencyPair;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.paralleltext.ParallelText;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.serviceexecutor.db.bilingualdictionary.DictionaryQuery;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * 
 * 
 */
public class DictionaryDataBase {
	/**
	 * 
	 * 
	 */
	public DictionaryDataBase(
			String tableName
			, ConnectionManager manager
			, String dbDictionary
			, int maxCount
			) {
		this.tableName = tableName;
		this.manager = manager;
		try{
			this.dbDictionary = DbDictionary.valueOf(dbDictionary);
		} catch(IllegalArgumentException e){
			this.dbDictionary = DbDictionary.POSTGRESQL;
		}
		this.maxCount = maxCount;
	}

	/**
	 * 
	 * 
	 */
	public Collection<Translation> getTranslation(
			Language headLang, Language targetLang, String headword
			, MatchingMethod matchingMethod
	) throws SQLException {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = null;
		try {
			String hl = headLang.getCode();
			String tl = targetLang.getCode();
			Collection<Translation> collection = new ArrayList<Translation>();
			con = manager.getConnection();
			sql = createBilingualDictionarySQL(hl, tl, headword, matchingMethod);
//			System.out.println(sql);		//###
			st = con.createStatement();
			rs = st.executeQuery(sql);
			Translation current = new Translation();
			Set<String> currentTargetWords = new HashSet<String>();
			while (rs.next()) {
				String head = rs.getString(hl);
				String target = rs.getString(tl);
				if(head == null || head.trim().length() == 0) continue;
				if(target == null || target.trim().length() == 0) continue;

				if(!head.equals(current.getHeadWord())){
					if(current.getHeadWord() != null){
						current.setTargetWords(currentTargetWords.toArray(new String[]{}));
						collection.add(current);
						if(collection.size() == maxCount) break;
					}
					current = new Translation(head, new String[]{});
					currentTargetWords.clear();
				}
				currentTargetWords.add(target);
			}
			if(current.getHeadWord() != null && collection.size() < maxCount){
				current.setTargetWords(currentTargetWords.toArray(new String[]{}));
				collection.add(current);
			}
//			System.out.println(collection.size() + " hits.");	//###
			return collection;
		} catch (SQLException e) {
			logger.log(Level.WARNING, "exception at SQL: " + sql, e);
			throw e;
		} finally{
			if(rs != null){
				try{
					rs.close();
				} catch (SQLException e){
				}
			}
			if(st != null){
				try{
					st.close();
				} catch (SQLException e){
				}
			}
			if(con != null){
				try{
					con.close();
				} catch(SQLException e){
				}
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public void getFirstTranslations(
			Language headLang, Language targetLang
			, DictionaryQuery[] queries, Translation[] result
	) throws SQLException {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = null;
		try {
			String hl = headLang.getCode();
			String tl = targetLang.getCode();
			con = manager.getConnection();
			StringBuilder s = new StringBuilder();
			for(int i = 0; i < queries.length; i++){
				DictionaryQuery q = queries[i];
				if(q.getHeadWord() == null || q.getHeadWord().length() == 0) continue;
				if(s.length() > 0){
					s.append(" UNION ALL ");
				}
				s.append("(");
				s.append(createBilingualDictionarySQL(
						i, hl, tl, q.getHeadWord()
						, q.getMatchingMethod(), q.getOrder()
						));
				s.append(" LIMIT 1)");
			}
			if(s.length() == 0) return;
			st = con.createStatement();
///			long start = System.currentTimeMillis();	//###
			sql = s.toString();
			rs = st.executeQuery(sql);
			int c = 0; //###
			while (rs.next()) {
				int index = rs.getInt(1);
				String head = rs.getString(hl);
				String target = rs.getString(tl);
				result[index] = new Translation(head, new String[]{target});
				c++;	//###
			}
//			System.out.println((System.currentTimeMillis() - start) + "ms: " + s);		//###
//			System.out.println(c + " hits.");
		} catch (SQLException e) {
			logger.log(Level.WARNING, "exception at SQL: " + sql, e);
			throw e;
		} finally{
			if(rs != null){
				try{
					rs.close();
				} catch (SQLException e){
				}
			}
			if(st != null){
				try{
					st.close();
				} catch (SQLException e){
				}
			}
			if(con != null){
				try{
					con.close();
				} catch(SQLException e){
				}
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public Collection<ParallelText> getParallelText(
			Language headLang, Language targetLang, String headword
			, MatchingMethod matchingMethod
			) throws SQLException
	{
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			String hl = headLang.getCode();
			String tl = targetLang.getCode();
			Collection<ParallelText> collection = new ArrayList<ParallelText>();
			con = manager.getConnection();
			String sql = createParallelTextSQL(hl, tl, headword, matchingMethod);
			st = con.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String head = rs.getString(hl);
				String target = rs.getString(tl);
				ParallelText text = new ParallelText(head, target);
				collection.add(text);
				if(collection.size() == maxCount) break;
			}
			return collection;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public Collection<AdjacencyPair> getAdjacencyPair(
			String category, Language language, String firstTurn
			, MatchingMethod matchingMethod
			) throws SQLException
	{
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			Collection<AdjacencyPair> collection = new ArrayList<AdjacencyPair>();
			con = manager.getConnection();
			pst = con.prepareStatement(createAdjacencyPairSQL(
					category, language, firstTurn, matchingMethod
					));
			int i = 1;
			if(category.length() > 0){
				pst.setString(i++, category);
			}
			pst.setString(i++, firstTurn);
			rs = pst.executeQuery();
			while (rs.next()) {
				String cat = rs.getString("category");
				String ft = rs.getString("firstTurn");
				String[] st = ArrayUtil.collect(
						rs.getString("secondTurns").split(",")
						, new Transformer<String, String>(){
							public String transform(String value)
									throws TransformationException {
								return StringUtil.unescape(value, ",", '%');
							}
						}
						);
				collection.add(new AdjacencyPair(cat, ft, st));
				if(collection.size() == maxCount) break;
			}
			return collection;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public String getTableName(){
		return tableName;
	}

	protected Connection getConnection()
	throws SQLException{
		return manager.getConnection();
	}

	protected DbDictionary getDbDictionary(){
		return dbDictionary;
	}

	protected int getMaxCount(){
		return maxCount;
	}

	private String createBilingualDictionarySQL(
			String headLang, String targetLang, String headword
			, MatchingMethod matchingMethod) {
		return createBilingualDictionarySQL(
				-1, headLang, targetLang, headword, matchingMethod, true);
	}

	/**
	 * 
	 * 
	 */
	private String createBilingualDictionarySQL(
			int index, String headLang, String targetLang, String headword
			, MatchingMethod matchingMethod, boolean order) {
		String headLangColumn = headLang;
		String targetLangColumn = targetLang;

		StringBuilder buf = new StringBuilder();
		buf.append(" SELECT ");
		if(index != -1){
			buf.append(index).append(", ");
		}
		buf.append(headLangColumn).append(", ");
		buf.append(targetLangColumn);
		buf.append(" FROM \"").append(tableName).append("\"");
		buf.append(" WHERE LOWER(").append(headLangColumn).append(")");
		String escapedHw = StringEscapeUtils.escapeSql(headword.toLowerCase());
		if (matchingMethod.equals(COMPLETE)) {
			buf.append(" = '").append(escapedHw).append("'");
		} else if(matchingMethod.equals(REGEX)){
			if(dbDictionary != null){
				switch(dbDictionary){
					case POSTGRESQL:
						buf.append(" ~* '").append(escapedHw).append("'");
						break;
					case MYSQL:
						buf.append(" REGEXP LOWER('").append(escapedHw).append("')");
						break;
					default:
						buf.append(" = '").append(escapedHw).append("' AND 0=1");
				}
			} else{
				buf.append(" = '' AND 0=1");
			}
		} else {
			String pre = "";
			String suf = "";
			if (matchingMethod.equals(PARTIAL)) {
				pre = PERCENT;
				suf = PERCENT;
			} else if (matchingMethod.equals(PREFIX)) {
				suf = PERCENT;
			} else if (matchingMethod.equals(SUFFIX)) {
				pre = PERCENT;
			}
			buf.append(" LIKE '")
				.append(pre).append(escapedHw).append(suf)
				.append("'");
		}
		buf.append(" AND ").append(targetLangColumn).append(" IS NOT NULL");
		buf.append(" AND ").append(targetLangColumn).append(" <> ''");
		if(order){
			buf.append(" ORDER BY ").append(headLangColumn)
				.append(",").append(targetLangColumn);
		}
		return buf.toString();
	}

	private String createParallelTextSQL(
			String headLang, String targetLang, String headword
			, MatchingMethod matchingMethod) {
		return createBilingualDictionarySQL(
				headLang, targetLang, headword, matchingMethod);
	}

	private String createAdjacencyPairSQL(String category
			, Language language, String firstTurn
			, MatchingMethod matchingMethod) {
		StringBuffer buf = new StringBuffer();
		buf.append(" SELECT category, firstTurn, secondTurns FROM \"");
		buf.append(tableName);
		buf.append("_");
		buf.append(language);
		buf.append("\" WHERE ");
		if(category.length() > 0){
			buf.append("category=? AND ");
		}
		buf.append("LOWER(firstTurn)");
		if (matchingMethod.equals(COMPLETE)) {
			buf.append(" = ? ");
		} else if(matchingMethod.equals(REGEX) && dbDictionary != null){
			switch(dbDictionary){
				case POSTGRESQL:
					buf.append(" ~* ? ");
					break;
				case MYSQL:
					buf.append(" REGEXP LOWER(?) ");
					break;
				default:
					buf.append(" = ? and 0=1");
			}
		} else {
			buf.append(" LIKE ? ");
			if (matchingMethod.equals(PARTIAL)) {
				firstTurn = PERCENT + firstTurn.toLowerCase() + PERCENT;
			} else if (matchingMethod.equals(PREFIX)) {
				firstTurn = firstTurn.toLowerCase() + PERCENT;
			} else if (matchingMethod.equals(SUFFIX)) {
				firstTurn = PERCENT + firstTurn.toLowerCase();
			}
		}
		buf.append(" order by firstTurn");
		return buf.toString();
	}

	private ConnectionManager manager;
	private DbDictionary dbDictionary;
	private String tableName;
	private int maxCount;
	private static final String PERCENT = "%";
	private static Logger logger = Logger.getLogger(DictionaryDataBase.class.getName());
}
