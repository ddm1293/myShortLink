<template>
  <div ref="vantaRef" class="vanta"></div>
</template>
  
<script setup>
  import { ref, onMounted, onBeforeUnmount } from 'vue'
  import * as THREE from 'three'
  import CLOUDS from 'vanta/src/vanta.globe';

  const vantaRef = ref()
  let vantaEffect = null
  onMounted(() => {
    vantaEffect = CLOUDS({
      el: vantaRef.value,
      THREE: THREE,
      mouseControls: true,
      touchControls: true,
      gyroControls: false,
      minHeight: 200.00,
      minWidth: 200.00,
      scale: 1.00,
      scaleMobile: 1.00,
      color: 0x3fc6ff,
      backgroundColor: 0x14152a
    })
  })
  onBeforeUnmount(() => {
    if (vantaEffect) {
      vantaEffect.destroy()
    }
  })
</script>
  
<style scoped>
  .vanta {
    position: absolute;
    top: 0;
    right: 0;
    left: 0;
    bottom: 0;
    z-index: 0;
  }
</style>