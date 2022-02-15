<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default{
  name: 'App',
  created() {
    const audio = new Audio()
    audio.src = 'http://guoyu-admin.windymuse.cn/static/%E5%B0%8F%E7%A8%8B%E5%BA%8F%E4%B8%8B%E5%8D%95%E5%95%A6.mp3'
    // setInterval(function() {
    //   audio.play()
    // }, 5000)
  }
}
</script>
