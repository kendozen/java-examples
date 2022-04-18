package cn.org.manta.enhance.biz;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * @author hwang
 * @description 业务类，这里实现一个内存缓存
 * @date 4/18/22 1:51 PM
 */
@Component
public class MemoryCache {
  private static final Map<Object, Object> CACHE = new HashMap<>();

  public Object get(Object key){
    if(CACHE.containsKey(key)){
      return CACHE.get(key);
    }
    return null;
  }

  public void put(Object key, Object obj){
    CACHE.put(key, obj);
  }
}
