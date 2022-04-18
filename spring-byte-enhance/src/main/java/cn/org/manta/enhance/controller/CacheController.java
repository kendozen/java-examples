package cn.org.manta.enhance.controller;

import cn.org.manta.enhance.biz.MemoryCache;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hwang
 * @description 缓存控制器
 * @date 4/18/22 3:05 PM
 */
@RequestMapping("/api/cache")
@RestController
public class CacheController {

  private static final String KEY = "KEY";
  @Resource
  private MemoryCache memoryCache;

  @PutMapping
  public void put(@RequestParam String value){
    memoryCache.put(KEY, value);
  }

  @GetMapping
  public Object get(){
    return memoryCache.get(KEY);
  }
}
