package Handlers;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import javax.imageio.ImageIO;

// this class loads resources on boot.
// spritesheets are taken from here.

public class Content {
	
	// special
	public static BufferedImage[][] Invisible = load("/Sprites/Enemies/invisible.gif", 31, 31);
	// special
	
	// text overlays
	public static BufferedImage[][] DialogBox = load("/Sprites/Objects/dialogbox.gif", 320, 71);
	public static BufferedImage[][] DialogIcon = load("/Sprites/Objects/dialogicon.gif", 23, 23);
	// text overlays
	
	// neutral objects
	public static BufferedImage[][] Portal = load("/Sprites/Objects/portal.gif", 31, 31);

	public static BufferedImage[][] PortalM = load("/Sprites/Objects/portalm.gif", 31, 31);
	// neutral objects
	
	// collectibles
	public static BufferedImage[][] Life = load("/Sprites/Objects/life.gif", 31, 31);
	public static BufferedImage[][] LifeShine = load("/Sprites/Objects/lifeshine.gif", 31, 31);
	
	public static BufferedImage[][] LifeM = load("/Sprites/Objects/lifem.gif", 31, 31);
	public static BufferedImage[][] LifeShineM = load("/Sprites/Objects/lifeshine.gif", 31, 31);
	// collectibles

	// player objects
	public static BufferedImage[][] PlayerCore = load("/Sprites/Player/playercore.gif", 31, 31);
	public static BufferedImage[][] PlayerFracture = load("/Sprites/Player/playerfracture.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece1 = load("/Sprites/Player/playerpiece1.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece2 = load("/Sprites/Player/playerpiece2.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece3 = load("/Sprites/Player/playerpiece3.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece4 = load("/Sprites/Player/playerpiece4.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece5 = load("/Sprites/Player/playerpiece5.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece6 = load("/Sprites/Player/playerpiece6.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece7 = load("/Sprites/Player/playerpiece7.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece8 = load("/Sprites/Player/playerpiece8.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece9 = load("/Sprites/Player/playerpiece9.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece10 = load("/Sprites/Player/playerpiece10.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece11 = load("/Sprites/Player/playerpiece11.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece12 = load("/Sprites/Player/playerpiece12.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece13 = load("/Sprites/Player/playerpiece13.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece14 = load("/Sprites/Player/playerpiece14.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece15 = load("/Sprites/Player/playerpiece15.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece16 = load("/Sprites/Player/playerpiece16.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece17 = load("/Sprites/Player/playerpiece17.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece18 = load("/Sprites/Player/playerpiece18.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece19 = load("/Sprites/Player/playerpiece19.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece20 = load("/Sprites/Player/playerpiece20.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece21 = load("/Sprites/Player/playerpiece21.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece22 = load("/Sprites/Player/playerpiece22.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece23 = load("/Sprites/Player/playerpiece23.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece24 = load("/Sprites/Player/playerpiece24.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece25 = load("/Sprites/Player/playerpiece25.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece26 = load("/Sprites/Player/playerpiece26.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece27 = load("/Sprites/Player/playerpiece27.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece28 = load("/Sprites/Player/playerpiece28.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece29 = load("/Sprites/Player/playerpiece29.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece30 = load("/Sprites/Player/playerpiece30.gif", 31, 31);
	public static BufferedImage[][] PlayerPiece31 = load("/Sprites/Player/playerpiece31.gif", 31, 31);

	public static BufferedImage[][] ParryShield = load("/Sprites/Player/parryshield.gif", 31, 31);
	public static BufferedImage[][] ParryShieldImpact = load("/Sprites/Player/parryshieldimpact.gif", 31, 31);
	public static BufferedImage[][] AttackWeak = load("/Sprites/Player/playerattackweak.gif", 31, 31);
	public static BufferedImage[][] AttackStrong = load("/Sprites/Player/playerattackstrong.gif", 31, 31);
	// player objects
	
	// boss
	public static BufferedImage[][] BossFrame = load("/Sprites/Boss/bossframe.gif", 217, 217);
	public static BufferedImage[][] BossGem = load("/Sprites/Boss/bossgem.gif", 31, 31);
	public static BufferedImage[][] BossGemMiddle = load("/Sprites/Boss/bossgemmiddle.gif", 31, 31);
	public static BufferedImage[][] BossShield = load("/Sprites/Boss/bossshield.gif", 131, 131);
	public static BufferedImage[][] BossShieldFlash = load("/Sprites/Boss/bossshieldflash.gif", 331, 251);
	public static BufferedImage[][] BossPortal1 = load("/Sprites/Boss/bossportal1.gif", 31, 31);
	public static BufferedImage[][] BossPortal2 = load("/Sprites/Boss/bossportal2.gif", 31, 31);
	public static BufferedImage[][] BossPortal3 = load("/Sprites/Boss/bossportal3.gif", 31, 31);
	public static BufferedImage[][] BossWall = load("/Sprites/Boss/bosswall.gif", 31, 217);
	public static BufferedImage[][] BossWallBall = load("/Sprites/Boss/bosswallball.gif", 31, 31);
	public static BufferedImage[][] BossBall1 = load("/Sprites/Boss/bossball1.gif", 31, 31);
	public static BufferedImage[][] BossBall2 = load("/Sprites/Boss/bossball2.gif", 131, 131);
	public static BufferedImage[][] BossBall3 = load("/Sprites/Boss/bossball3.gif", 31, 31);
	public static BufferedImage[][] BossBall4Chase = load("/Sprites/Boss/bossball4chase.gif", 31, 31);
	public static BufferedImage[][] BossBall4Split = load("/Sprites/Boss/bossball4split.gif", 31, 31);
	public static BufferedImage[][] BossBall5GemRed = load("/Sprites/Boss/bossball5gemred.gif", 31, 31);
	public static BufferedImage[][] BossBall5GemGreen = load("/Sprites/Boss/bossball5gemgreen.gif", 31, 31);
	public static BufferedImage[][] BossBall5Flash = load("/Sprites/Boss/bossball5flash.gif", 31, 31);
	public static BufferedImage[][] BossBall6Particle = load("/Sprites/Boss/bossball6particle.gif", 31, 31);
	public static BufferedImage[][] BossBall6Explosion = load("/Sprites/Boss/bossball6explosion.gif", 131, 131);
	public static BufferedImage[][] BossBall7 = load("/Sprites/Boss/bossball7.gif", 31, 31);
	public static BufferedImage[][] BossBall7Explosion = load("/Sprites/Boss/bossball7explosion.gif", 131, 131);
	public static BufferedImage[][] BossBall7Particle = load("/Sprites/Boss/bossball7particle.gif", 31, 31);
	// should probably put everything into one big file next time
	public static BufferedImage[][] BossGemPiece1 = load("/Sprites/Boss/bossgempiece1.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece2 = load("/Sprites/Boss/bossgempiece2.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece3 = load("/Sprites/Boss/bossgempiece3.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece4 = load("/Sprites/Boss/bossgempiece4.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece5 = load("/Sprites/Boss/bossgempiece5.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece6 = load("/Sprites/Boss/bossgempiece6.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece7 = load("/Sprites/Boss/bossgempiece7.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece8 = load("/Sprites/Boss/bossgempiece8.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece9 = load("/Sprites/Boss/bossgempiece9.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece10 = load("/Sprites/Boss/bossgempiece10.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece11 = load("/Sprites/Boss/bossgempiece11.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece12 = load("/Sprites/Boss/bossgempiece12.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece13 = load("/Sprites/Boss/bossgempiece13.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece14 = load("/Sprites/Boss/bossgempiece14.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece15 = load("/Sprites/Boss/bossgempiece15.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece16 = load("/Sprites/Boss/bossgempiece16.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece17 = load("/Sprites/Boss/bossgempiece17.gif", 31, 31);
	public static BufferedImage[][] BossGemPiece18 = load("/Sprites/Boss/bossgempiece18.gif", 31, 31);
	
	public static BufferedImage[][] BossFrameM = load("/Sprites/Boss/bossframem.gif", 217, 217);
	public static BufferedImage[][] BossGemM = load("/Sprites/Boss/bossgemm.gif", 31, 31);
	public static BufferedImage[][] BossGemMiddleM = load("/Sprites/Boss/bossgemmiddlem.gif", 31, 31);
	public static BufferedImage[][] BossShieldM = load("/Sprites/Boss/bossshield.gif", 131, 131);
	public static BufferedImage[][] BossShieldFlashM = load("/Sprites/Boss/bossshieldflashm.gif", 331, 251);
	public static BufferedImage[][] BossPortal1M = load("/Sprites/Boss/bossportal1m.gif", 31, 31);
	public static BufferedImage[][] BossPortal2M = load("/Sprites/Boss/bossportal2m.gif", 31, 31);
	public static BufferedImage[][] BossPortal3M = load("/Sprites/Boss/bossportal3m.gif", 31, 31);
	public static BufferedImage[][] BossWallM = load("/Sprites/Boss/bosswallm.gif", 31, 217);
	public static BufferedImage[][] BossWallBallM = load("/Sprites/Boss/bosswallballm.gif", 31, 31);
	public static BufferedImage[][] BossBall1M = load("/Sprites/Boss/bossball1m.gif", 31, 31);
	public static BufferedImage[][] BossBall2M = load("/Sprites/Boss/bossball2m.gif", 131, 131);
	public static BufferedImage[][] BossBall3M = load("/Sprites/Boss/bossball3m.gif", 31, 31);
	public static BufferedImage[][] BossBall4ChaseM = load("/Sprites/Boss/bossball4chasem.gif", 31, 31);
	public static BufferedImage[][] BossBall4SplitM = load("/Sprites/Boss/bossball4splitm.gif", 31, 31);
	public static BufferedImage[][] BossBall5GemRedM = load("/Sprites/Boss/bossball5gemredm.gif", 31, 31);
	public static BufferedImage[][] BossBall5GemGreenM = load("/Sprites/Boss/bossball5gemgreenm.gif", 31, 31);
	public static BufferedImage[][] BossBall5FlashM = load("/Sprites/Boss/bossball5flash.gif", 31, 31);
	public static BufferedImage[][] BossBall6ParticleM = load("/Sprites/Boss/bossball6particle.gif", 31, 31);
	public static BufferedImage[][] BossBall6ExplosionM = load("/Sprites/Boss/bossball6explosionm.gif", 131, 131);
	public static BufferedImage[][] BossBall7M = load("/Sprites/Boss/bossball7m.gif", 31, 31);
	public static BufferedImage[][] BossBall7ExplosionM = load("/Sprites/Boss/bossball7explosionm.gif", 131, 131);
	public static BufferedImage[][] BossBall7ParticleM = load("/Sprites/Boss/bossball7particlem.gif", 31, 31);
	// boss
	
	// enemies
	public static BufferedImage[][] Spikes = load("/Sprites/Enemies/spikes.gif", 31, 31);
	public static BufferedImage[][] Star = load("/Sprites/Enemies/star.gif", 31, 31);
	public static BufferedImage[][] Triangle = load("/Sprites/Enemies/triangle.gif", 31, 31);
	public static BufferedImage[][] Halfmoon = load("/Sprites/Enemies/halfmoon.gif", 31, 31);
	public static BufferedImage[][] Darkmist = load("/Sprites/Enemies/darkmist.gif", 31, 31);
	public static BufferedImage[][] Mine = load("/Sprites/Enemies/mine.gif", 31, 31);
	public static BufferedImage[][] Shield = load("/Sprites/Enemies/shield.gif", 31, 31);
	public static BufferedImage[][] Sentry = load("/Sprites/Enemies/sentry.gif", 31, 31);
	public static BufferedImage[][] Square = load("/Sprites/Enemies/square.gif", 31, 31);
	
	public static BufferedImage[][] SpikesM = load("/Sprites/Enemies/spikesm.gif", 31, 31);
	public static BufferedImage[][] StarM = load("/Sprites/Enemies/starm.gif", 31, 31);
	public static BufferedImage[][] TriangleM = load("/Sprites/Enemies/trianglem.gif", 31, 31);
	public static BufferedImage[][] HalfmoonM = load("/Sprites/Enemies/halfmoonm.gif", 31, 31);
	public static BufferedImage[][] DarkmistM = load("/Sprites/Enemies/darkmistm.gif", 31, 31);
	public static BufferedImage[][] MineM = load("/Sprites/Enemies/minem.gif", 31, 31);
	public static BufferedImage[][] ShieldM = load("/Sprites/Enemies/shieldm.gif", 31, 31);
	public static BufferedImage[][] SentryM = load("/Sprites/Enemies/sentrym.gif", 31, 31);
	public static BufferedImage[][] SquareM = load("/Sprites/Enemies/square.gif", 31, 31);
	// enemies
	
	// enemy objects
	public static BufferedImage[][] EnemyExplosion = load("/Sprites/Enemies/explosion.gif", 31, 31);
	public static BufferedImage[][] MineExplosion = load("/Sprites/Enemies/mineexplosion.gif", 131, 131);
	public static BufferedImage[][] ShieldBall = load("/Sprites/Enemies/shieldball.gif", 141, 141);
	public static BufferedImage[][] SentryGemGreen = load("/Sprites/Enemies/sentrygemgreen.gif", 31, 31);
	public static BufferedImage[][] SentryGemRed = load("/Sprites/Enemies/sentrygemred.gif", 31, 31);
	public static BufferedImage[][] SquareLock = load("/Sprites/Enemies/squarelock.gif", 131, 131);

	public static BufferedImage[][] EnemyExplosionM = load("/Sprites/Enemies/explosionm.gif", 31, 31);
	public static BufferedImage[][] MineExplosionM = load("/Sprites/Enemies/mineexplosionm.gif", 131, 131);
	public static BufferedImage[][] ShieldBallM = load("/Sprites/Enemies/shieldballm.gif", 141, 141);
	public static BufferedImage[][] SentryGemGreenM = load("/Sprites/Enemies/sentrygemgreenm.gif", 31, 31);
	public static BufferedImage[][] SentryGemRedM = load("/Sprites/Enemies/sentrygemredm.gif", 31, 31);
	public static BufferedImage[][] SquareLockM = load("/Sprites/Enemies/squarelock.gif", 131, 131);
	// enemy objects

	//public static BufferedImage[][] MineExplosionT = load("/Sprites/Enemies/mineexplosion.png", 131, 131);
	//public static BufferedImage[][] BossBall6ExplosionT = load("/Sprites/Boss/bossball6explosion.png", 131, 131);
	
	
	public static BufferedImage[][] load(String s, int w, int h) {
		BufferedImage[][] ret;
		try {
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));
			int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;
			ret = new BufferedImage[height][width];
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
	
	public static BufferedImage[] convertToGrayScale(BufferedImage[] imagesToConvert) {
		BufferedImage[] grayScaleImages = new BufferedImage[imagesToConvert.length];
		BufferedImage tempColorImage = null;
		BufferedImage tempGrayScaleImage = null;
		ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		
		for(int i = 0; i < imagesToConvert.length; i++) {
			tempColorImage = imagesToConvert[i];
			tempGrayScaleImage = new BufferedImage(tempColorImage.getWidth(), tempColorImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
			op.filter(tempColorImage, tempGrayScaleImage);
			grayScaleImages[i] = tempGrayScaleImage;
		}
		
		return grayScaleImages;
	}
}
