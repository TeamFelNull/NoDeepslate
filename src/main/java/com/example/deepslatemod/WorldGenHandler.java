package com.example.deepslatemod;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class WorldGenHandler {

    /**
     * 深層岩 → 通常石 の対応表
     * 深層岩バリアントの鉱石も、対応する石バリアントに置換します
     */
    private static final Map<BlockState, BlockState> REPLACEMENTS = Map.ofEntries(
        // 深層岩本体
        Map.entry(Blocks.DEEPSLATE.defaultBlockState(),         Blocks.STONE.defaultBlockState()),
        Map.entry(Blocks.COBBLED_DEEPSLATE.defaultBlockState(), Blocks.COBBLESTONE.defaultBlockState()),

        // 深層岩 鉱石 → 石 鉱石
        Map.entry(Blocks.DEEPSLATE_COAL_ORE.defaultBlockState(),     Blocks.COAL_ORE.defaultBlockState()),
        Map.entry(Blocks.DEEPSLATE_IRON_ORE.defaultBlockState(),     Blocks.IRON_ORE.defaultBlockState()),
        Map.entry(Blocks.DEEPSLATE_COPPER_ORE.defaultBlockState(),   Blocks.COPPER_ORE.defaultBlockState()),
        Map.entry(Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState(),     Blocks.GOLD_ORE.defaultBlockState()),
        Map.entry(Blocks.DEEPSLATE_REDSTONE_ORE.defaultBlockState(), Blocks.REDSTONE_ORE.defaultBlockState()),
        Map.entry(Blocks.DEEPSLATE_EMERALD_ORE.defaultBlockState(),  Blocks.EMERALD_ORE.defaultBlockState()),
        Map.entry(Blocks.DEEPSLATE_LAPIS_ORE.defaultBlockState(),    Blocks.LAPIS_ORE.defaultBlockState()),
        Map.entry(Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState(),  Blocks.DIAMOND_ORE.defaultBlockState()),

        // 感染深層岩
        Map.entry(Blocks.INFESTED_DEEPSLATE.defaultBlockState(),     Blocks.INFESTED_STONE.defaultBlockState())
    );

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        // サーバーサイドのみ処理（クライアントでは何もしない）
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;

        ChunkAccess chunk = event.getChunk();

        // 深層岩が生成されるY範囲: ワールド最低高度(-64) から Y=16 まで
        // ※ バニラでは境目付近（Y=0〜8前後）に深層岩が混在するため、
        //    余裕を持ってY=16までスキャンする
        int minY = serverLevel.getMinBuildHeight(); // -64
        int maxY = 16;

        int chunkMinX = chunk.getPos().getMinBlockX();
        int chunkMinZ = chunk.getPos().getMinBlockZ();

        boolean modified = false;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = minY; y <= maxY; y++) {
                    BlockPos pos = new BlockPos(chunkMinX + x, y, chunkMinZ + z);
                    BlockState current = chunk.getBlockState(pos);

                    BlockState replacement = REPLACEMENTS.get(current);
                    if (replacement != null) {
                        chunk.setBlockState(pos, replacement, false);
                        modified = true;
                    }
                }
            }
        }

        // 変更があったチャンクをdirty（保存対象）にする
        if (modified) {
            chunk.setUnsaved(true);
        }
    }
}
