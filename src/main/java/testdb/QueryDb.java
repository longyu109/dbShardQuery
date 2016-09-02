package testdb;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.zip.CRC32;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class QueryDb {

	public static void main(String[] args) {
		shard_new();
	}

	public static void shard_new() {
		try {
			Long seed = 314272L;

			String jdbcUrl = "jdbc:mysql://192.168.3.161:3306/ting_content?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
			String username = "naliworld";
			String password = "password!";
			String node_table = "tb_track_node";

			SingleConnectionDataSource dsConnectionDataSource = new SingleConnectionDataSource(jdbcUrl, username,
					password, true);

			JdbcTemplate jdbcexe = new JdbcTemplate(dsConnectionDataSource);

			String sql = "select * from " + node_table + " order by node;";

			List<Map<String, Object>> n = jdbcexe.queryForList(sql);

			TreeMap<Long, String> treeNodes = new TreeMap<Long, String>();

			for (Map<String, Object> e : n) {

				Long node_i = (Long) e.get("node");
				String table_name = (String) e.get("table_name");
				treeNodes.put(node_i, table_name);

			}

			CRC32 c = new CRC32();
			c.update(String.valueOf(seed).getBytes());
			Long key = c.getValue();

			// Long key = ShardUtil.hash(seed);

			// ================
			NavigableMap<Long, String> ret = treeNodes.tailMap(key, true);
			if (ret == null) {
				System.err.println("====== table 0");
			} else {
				System.err.println("====== table " + treeNodes.get(ret.firstKey()));
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
