package io.undervolt.gui.user;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.notifications.Notification;
import org.json.JSONObject;

import java.awt.*;

public class User {

    public enum Status {
        ONLINE, AWAY, BUSY, OFFLINE;
    }

    private final String username, image, alias, banner, createDate;
    private Status status;
    private final String countryCode;
    private final boolean developer;

    public User(final String username, final Status status, String countryCode, boolean developer, String image, String alias, String banner, String createDate) {
        this.status = status;
        this.username = username;
        this.countryCode = countryCode;
        this.developer = developer;
        this.image = image;
        this.alias = alias;
        this.banner = banner;
        this.createDate = createDate;
    }

    public final String getUsername() {
        return this.username;
    }

    public final Status getStatus() {
        return this.status;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public boolean isDeveloper() {
        return developer;
    }

    public String getAlias() {
        return alias;
    }

    public String getBanner() {
        if(image == null || image.equals("default")) return null;
        else return image;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getImage() {
        if(image == null) return null;
        if(image.equals("default")) return "a,iVBORw0KGgoAAAANSUhEUgAABdwAAAXcCAMAAAAP67xWAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAMAUExURQAAAA8OCB8dESEfEi8sGT05ID87IkpGJ05KKlZRLl5ZMl9ZM2ReNW5oO3BpPH53Q393RIuCSo6GTJiOUZ6VVJ+VVaacWK6kXbKoX76zZcK2Z87CbtDDb97Rdt/Rd+7ff+7gf/7viAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPx8SnUAAAAJcEhZcwAALEoAACxKAXd6dE0AAE56SURBVHhe7d1tY9RKtqZp7KYZuhgamqEOw/HxGe///yfHNgE4/ZpSSk9ErLyuT1W1t5xJlrktL4VC7/4BoBxxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3AEKEneAgsQdoCBxByhI3DkX/9+X9//7v9t/hvLEnfNw8/Xi3a0PP9t/h+LEnbPw4/Iu7XfknfMg7pyBnx9a2e/JO+dA3Cnv5lOr+h8frts/grLEneLasP2RT/JOceJObd//DNsfkXdqE3cqOxy2PyLvVCbu1HX9ZNh+6PJr+xehHnGnqueH7Ycu/93+ZahG3CnqxWH7IXmnKHGnpFeH7YfeX7VjoBJxp6C3hu2PuLJKQeJOOccM2x+Rd8oRd6o5cth+6MLCGYoRd2pZMGw/5MoqtYg7lVx/bKleQ96pRNypY8Ww/dBHo3fKEHfK+H5i2u+4skoV4k4Rq4ftj3yVd0oQd0o4adh+yOidEsSdAk4eth+SdwoQd+a3xbD9kEc1MT1xZ3ZbDdsPubLK5MSduW04bH/k6017CZiRuDOzjYfth4zemZm4M7Hth+2H5J15iTvT2mfYfsjonVmJO5Pab9h+SN6Zk7gzpV2H7YfsBsyUxJ0Z7T1sP2T0zoTEnfkkhu2HLn+2l4ZZiDuzSQ3bDxm9MxlxZy7BYfsj8s5UxJ2pZIfth4zemYm4M5H8sP2QvDMPcWcafYbth2wXySzEnUn0G7YfMnpnDuLOHH5ctrj256YmZiDuzKD3sP2Q0TsTEHfGd/OpVXUYbmpieOLO6EYZth8yemdw4s7gBhq2H/os74xM3BnaWMP2Q0bvjEzcGdh4w/ZD8s64xJ1hjTlsP/TRbIZBiTuj+jnqsP2QK6uMSdwZ08jD9kOXbmpiROLOiG4+t3JOweidAYk745lh2H7o0myG0Yg7w5lk2H7I6J3BiDuDuZpm2P7IV3lnJOLOUOYath8yemck4s5Iphu2H7r80f4c0J24M44ph+2HjN4ZhbgziutZh+2H5J0xiDtjmHnYfsjonSGIO0P4Nvew/ZC8MwBxZwAFhu2H7CdGd+JOd0WG7YeM3ulM3Ons5mvL4f4+fXnf/lOA/cToS9zpKzds/3D3UOvrf7X/FmD0Tk/iTk8/Y6fS92m/c/0lN+B/bzZDN+JOP7lh+8XDk+hk3o3e6UXc6SU3bL/4ctNeswkOZy6M3ulD3OkkN2x/7uw5mHejd7oQd7rIPUXvz7D9kR9G75Qm7nSQG7a/tk/jl/bvBBi9EyfuxOWeovdk2H7o+mP79wKM3gkTd9K+x4btn19N+53vudmM0TtZ4k5W/2H7gevgZpSeok2SuJOUG4RcHpP2O9fBLQmM3skRd3KGGbYfCs5mjN6JEXdichF9e9h+4Ca4bsbonRBxJ2SwYfuh6H5iZjMkiDsRN59a2nb32sr2VyRnM0bvBIg7AYMO2w8FtxN793X1u4QjiTv7y93pf9I5sb3eqUTc2dvQw/ZD18GT98ur9qKwC3FnXzexu4QO9mxfy+idKsSdXc0wbD8UXBZp9M6OxJ0d/YydB3/Y7izY6J0SxJ3dXMWG7e9PHbYfio7et33r8Ju4s5PgsP1be8ntGL0zPXFnH7mn6C3ca+BI0VXv8s72xJ09TDlsP2T0ztzEne0Fn6K358Q6+aAmo3e2Ju5s7eZrC9buNlv++BKjd+Yl7mwsN2z/FFglnsz7Z3lnQ+LOpibaa+A41/Z6Z07izoZyw/ZN9ho4jiurTEnc2cwUG/uukfuZZfTOZsSdrXyPDdt3W/74IldWmY64s43csL3PqsFg3s1m2IK4s4XckvAd9ho4jiurzEXcOV1u2L7TXgPHSV5Z/Wg2w4nEnZPlJhah5Y8vcmWVeYg7JwoO23+0l+woOXr/2l4T1hB3TnL9qaVod+Hljy9yZZU5iDsnCA7b88sfX3LzJfaHHuhPzXTEnfV+xE5iN37U0omSV1aN3llJ3FkrN2zvtvzxRcG8Xxi9s4q4s85NbNjedfnji3KPIzF6ZxVxZ43ksP2qveZokldWzWZYTNxZITdsH/oJRcG8G72zlLizWHDYPsjyx5cktyT4OvZHwXDEnYVuPrfa7G+C09XglVWjdxYRdxZJDttHnsj8lcz7HJ8IYxB3lsitERlv+eOLggtnjN45mrhzvKvcvlmDD9sfCV5Z9RRtjiTuHCs4bJ/vrvtc3o3eOY64c6TcsH3K0fJ17kefvHMMcecowWH7l/aSswleWTV6523izhGCD6kYcq+BI+Xu7ZJ33iTuvCk5bJ98sd+33Oh9ntVE9CHuvOVbbtg+wKOWTuSmJkYh7rwuOWwvcYP9dW4241EevELceU1w2P6pzN4pwSc1Gb3zInHnZTdfW0P2N/uw/UBwNvPOozx4gbjzIsP21TxFm+7EnRf8fN/qsbsiw/YDyZN3+4nxHHHnWVa2nyp48m70zjPEnWfY2HcDyZN3eecJceep3LC9btrvfGt/ygSjdx4Rdx7LPUWvfJCCa96N3nlE3Dl0/bG1YncfzuBc8zp2VfqO2QwPiDsPBYftn9pLFpccvL9791Xe+U3ceeB77t7Kd9/baxZ33f64IUbv/Cbu/JEbtt/53F61tuxc5o7RO7+IO831p1aHlHOYIASfnf2X0Tt3xJ17wWH7b+9r3rz0QHAj/ENG74g7vwSfIfRX9UuqXU7bfzF6R9y5lR22/1V6R8Nup+2/lNuLjaXEnZv0sP2vwufuHU/bG6P3Myfu567DsP2BD0Xn7jexW8FeI+9nTdzPXO8TzMuS/cltzvM6T9E+Z+J+1noN2x+4qHfp76r/p/qHK6vnS9zPWOdLfr8Vu6wafDbhUTxF+1yJ+/nqOmx/qNRopv+F1CeM3s+TuJ+rkSJ0UWY0HHyA1QIXnqJ9jsT9PI00Fr5T5ORymF+GHjN6P0Pifo4GGbY/VKE+A05k/rq8au+ScyHuZ2iUhXqHPk5+8t7xXrDjGL2fGXE/O+OeX049GR7zJ+Yh+4mdFXE/M2Ne8WvmHR38jG/bvorR+zkR97My2hrsJ+YcHQw/kflL3s+HuJ+TCUYHM94wP8NE5i+j93Mh7udjgL0GjjHbXrWTTGQekPfzIO7nYuhh+6GZ4jPRROavSzc1nQNxPw99N/ZdbJq8z/Wx/mX0fgbE/Sx8n61Bl19n2Oh96LuW3vDebKY6cT8DkwzbD41/bjnRoOtZRu/FiXt510M8FGiFsfM+2aDrWW5qKk3ci5u6Qe/Hvalp5onMX0bvlYl7bd8nb9Cgo4PZJzJ/zbbwlOOJe2VTDtsfGTDvw9/nu4jRe1XiXteUS7CfGm5R9lw3pB5B3msS96oqXPBrhpoMV/ht6LEZ93zgTeJeVI0Lfr8Nk/civw094cpqQeJeUr3TyzEe5VHnt6EnPpjNVCPuBQWfonfx+VvqV4T+k+Hgb0MfPn/7V/oHidF7MeJeT+708uLL3SYBseWWfe+5yS1//PWx3n6w6d+/7CdWirhXkzu9/DunTeW942Q4t/zxw7//bqtz9a/2P4YYvVci7rUETy8fduD6S+i3hV71iS1//PCzvWJznc77o9dnXuJeSW7Y/nty8EcsQj0u/MWuTz9O+53rf+VG/XeM3qsQ90Jip5dP0n4nlvd0fWI7rz2X9jvXX7J5/yzvJYh7Gblh++dn0n4nNhNKXlmN3Qz2UtrvhPNu9F6CuBeRG7a/Nhepd2U19Sd6Le135J2lxL2E4FqONyJUK++pYftbn+qd6y/tX84wep+euFcQG7Yfk9TUKeb+96ym9ho4doVKeOWMvE9O3OcXW8vx7HXUp4pcWU0N25f8DpLNu/3E5ibus8s9Re+l66hPpRp0seMtlT8yv4Ac+QPzj3Dejd4nJu5zy23se8xY+K/U0p296hP6bWhp2u9k8/7ebGZa4j6177Fh+6K034ldWV38zt52HRq2f1qe9jtXsZVRd4zeZyXuExtt2P5I6srq1vVJ/Tb0Yf3jv1M/Oe/tOfxiR+I+rdTp5ZJh+6HYAGHTWypD4Vw25noimnej9ymJ+6RGHbYfSuV9u/qEfhva4A1n8242Mx9xn1NoLcft3+of7RVXSr3RbfIeus933ZjrsexdTUbv0xH3GQ0+bD+UOsM8vT6p34bWjrmeyC6c+brV2yZD3OeT29h3m5tAb1JnmCeO3kP3+W66Z3E070bvcxH36cSG7e83W2Q4w+g9tDL/xOuoTwUf7Hr7Aa9f4EOcuE8m9nf5YtN7z6/fty+7s7WXCFI3Le1x7hu9smr0Pg9xn0puY9/N5sK/jTx6D60q3eY66jOiee/7mHKOJ+4TyQ3bNx8e3Pk2aN5T11F3POmNLpwxep+EuM8j9xS9nf72xkbvi26pTF1H3ePn5V/ZK6v7/lnYhrjP4mdoav3u3V7Dg1vjXVkNXcMInO2mLmvcM3qfgLjPYYyn6G0gNR4+7uRy4M0fV4iO3j1Fe3jiPoPcXgOBX7jHubKa+omZO81N5t3ofXTiPoHcsD1yhnmdui78elRTl6d3HrYfclMTf4j78GJ7Dew9kfkrlqCX8576ZWivi9Mviubd6H1k4j643FP0oksgYk9qen5VdiztmWH7oeg9q/I+LnEfWm7YHs9Q7MrqM3faph5gFftV6JHo6N1TtEcl7iOLbezbI0OxG28ej4ZjP1b6rQZ3UxPiPrLcsL1ThmLj4Yf5SaV92715FouO3rfZPZSNifuocnsNdBkM/3KV+tXk8t+/+hMbWGy+N89isc/2jtH7gMR9ULFhe7fB8C+x2l5+us5dwYguf3xRdPTuKdrDEfch5RY8dBwMN9GHxUXElz++yOj9nIn7gHJ7DXScyPwVHQ8H9J/I/BX9bPv+Dshj4j6c3LB9mL+N0fHwzkYrnNH72RL30cT2GhhgIvNXcjy8p7VPgtpT8rO9MHofh7iPJbf8cc+dfVeIPUV7V2N9pr/FNvO5Y/Q+DHEfSehxb3fGm4/OP3ofd+Yc/WwvzWbGIO7jyK3UG2g9x0O5G3L3MNKU66no3MvofQjiPozgfk8jrec4MPHofcyJzAPRudfX0T+NcyDug8gtfxzkDpvnzTqbmWEVYHY2Y/TenbgPIbj8sfOeJ2+KPgp0IyOukXlOdO51edVelU7EfQS55Y/vPo7/+/J0s5lhp1xPfcl9pxm99ybu/QWXP4590e+36NK9k32Y6gw1O/d6/lEpZIh7b8Hlj0NsNnCUeWYzo0+5nor+YmT03pG49xVc/jjFRb8/JpnNTDSR+Su6bmaW6xEFiXtXweWPYy5tf9kMs5n3U0y5nso9mPeO0Xsn4t5RcPnjjKeY14OfvF98aW90QtlfjD7Lew/i3k1w+eOsp5hDz2ammnI9kb2wavTeg7j3knoC/615TzHHvafpYvpRcvYXo8v/aC9LjLj3EVz+OPcp5qAn71NeSH0semH1f7QXJUbcewguf5z+FDM5vTrWHLcLvC150Ufc48S9g+DyxwqnmMNdWJ3mdoG35X4xEvc4cY/7z2CripxiDvUgj7nuSH1L7KqGuMeJe9z/aN/tAWVOMcc5eZ/vjtS3hE7exT1O3ONicZ97rd6hUSbvlT7T3zIn7+IeJ+5xobhXO8UcYdlMvdP2XxKfrbjHiXtcJu71TjH7j2Yqnrb/Ejh5F/c4cY9LxH3+W2ye03c0U/W0/ZfdT97FPU7c4wJxL3GLzTN6rpp5X/a0/Ze9NxMT9zhxj9s97lVusXnG9/ZHzKv68/KBfX90inucuMftHffSGepU95pjrsd2vaoh7nHiHrdv3Auftt/rMpm5LD6S+e1mx9GMuMeJe9yuca8/PejwAL5zafut/X52inucuMftGPfqp+13Opy6T/xQjsV2W5Ek7nHiHrdf3P9V/6LfP/8EN9T8o/QiyEO7XdQQ9zhxj9sr7hff2wuU9u/2p8362l69vP2uqYp7nLjH7RT36uuwf/nW/rRpdW9OPbDjehlxjxP3uH3i/rl99dKSD5d45CyeAvpzxwcNiHucuMftEvezuOb3LfiQk6c+lT953/XXInGPE/e4XeJ+BifuHU/bf7moPXnfc5H7LXGPE/c4Y5lV+p62/1J5NvNz553DxD1O3ONcUF3hZ+/T9uZj0U95/4ehiHucuMftFPfKp5WjPIfpTsnR+96n7bfEPU7c4/aKe929BwLpWaJc3iM/O8U9Ttzj9ov7u8tSD+Zvul9IfapW3r9HrmaIe5y4x+0Y94K3Ut58HeBC6lN18n4V+tkp7nHiHrdr3KvtYDjYROaBGnnPXc0Q9zhxj9s37qUm73s/+u00X6fPe/LXInGPE/e4veNeZ+PfMScyf11Ovlvk7g/Ffkjc48Q9bve4FxkZjDuR+Wvm5afhWwfEPU7c4wJxL7DmfcA1Ms+a9ZOOT7zEPU7c4xJxn/3kfdA1Ms+acTfgDp+vuMeJe1wm7u8uJh4IzzCReWC6H6SZle2HxD1O3ONCcX837arIWSYyD0yV9z779Ih7nLjHxeI+56rIm6/tzU9lnoUzvX50inucuMcF4z7h5b4eE4NNzPFR97uYIe5x4h6XjPts8+BRdvZdZYKdfTpuii/uceIel437TNvNjLSz7yqD/yTt+qNT3OPEPS4d92n2ipxo+eOLBs57570cxD1O3OPicZ9jNjPZ8seXjHpltfudA+IeJ+5xHeI+/tW+CZc/vmTIz7r/dWpxjxP3uB5xv03OyCfvcy5/fNFwj7Md4Tq1uMeJe1yfuI88m5l2+eOLhvqsx9g4WdzjxD2uV9xHnc1MvfzxRV9HuX9slG16xD1O3OO6xX3I2cz1p/beqhnkR+kwvxWJe5y4x3WM+3Czmdxp5ftPX75/yf6OcPmj/Sn7Gei3InGPE/e4rnEfazbzI7P88eLD9zYkufkerV3nH6VDPaVQ3OPEPa5v3G/zPso9TaHTyg/fDsbf1/9KLqjvmPdRhu2NuMeJe1zvuA8ym7nJDNs/PPNA2eSzQ7v9pjTaEiRxjxP3uP5xf3fZfb+Z0Gnlc2m/8yM4nemS9/GWIIl7nLjHDRD37qP3zLD9pbTfufpX+5cCPqZ/Uxrxfl9xjxP3uCHi3nU2kzmtvHwl7Xeug3mPftiDDdsbcY8T97hB4t7tPpvMsP3iS3u5VwTzHhyEddyz/TXiHifuccPEvctsJjVsP+5UOZn3zIf98317vdGIe5y4x40T9w5bXGWG7RfH77sbzHvgwx54c01xjxP3uJHiHp4Gh9ZwLLuAGcz7zh/20E+yEvc4cY8bK+7BaXBoZftbF1KfyuX9YscPe8zrqH+Ie5y4xw0W99Q0ONWeL2suE+fyvtuHPfqTrMQ9Ttzjhot7ZCF2aBuZIy+kPhXM+x4f9ng3LT0m7nHiHjdg3HefBofac3HKPoy5Pm7+Yc+wb7K4x4l73JBx3/XBzqkLfZ9PXLif23Nm09H74MP2RtzjxD1uzLjvNw1OtWf1ROaBWN43/LCT26CdQNzjxD1u1Lhvk8cnQhf6tnoyRi7v2+y8HB22X3z4tPrjEfc4cY8bN+47TIOvQsP2VWtknnX9JTXk2ODDjg7bP/y8/ZC/tf+ymLjHiXvcyHHfehocGrZv+ytHbuHMibv7JIftv3983rT/vpi4x4l73Nhx33IaHNrCavldS2+J5f2kDzv4OI4Pf9/n2rmMuMeJe9zgcd9sfB0atm84kXngKrX/1urRe3DYfrAv/tqfe+IeJ+5xw8d9m2lwqD27LdCPXVld9ScI7hD26JEna4fu4h4n7nETxP3kZsaG7ZtPZB6I5f3r0g87uEPYk0/4R/sHS4l7nLjHrY375XV0RfPi4jwQGrYv2Nl3lesv7YX2tmz0HryO+syE7rr9o6XEPU7c49bG/ePtscm8r77Yl9rC6tQbUo8w4pXV3PfAs5cz1i6XEfc4cY9bG/dfz40bPu+pafAud1w9dZ36vI/88wSvo77ww3Plrw3iHifucWvj/r0dH8370pn2zdd25M62X/74otjnfcR1jl5LZB5auY5I3OPEPW5t3P/+ZUvmfdkJcmrYvsvyxxd9GSTv1x/bv7e/Vy5nrHwT4h4n7nGnxz2b9+MXzqQezhwYth/K3bP6yoedfIjeaz/SV34W4h4n7nFr437w9+0mdjZ567i8x4btuYnMX9exm5peWKWU3Grg9ZnXyiVE4h4n7nFr4/7odDV3Nnnr7bzHhu1r1/CcKvbL0nN5j27Z/sbMa+VdTOIeJ+5xa+PeDv9rpLzXHLYfim0X+Tjv0bS/eZHle/sXFxL3OHGP2yzuuUnIvVfynlrEcdx8aDe5n6YP8x5N+xH7Cv1s/+pC4h4n7nEr437ZDj+UvLL6UltLD9sPxVa9v7toH3Y07W9NZO5dtX93IXGPE/e4TePeP++p+lz0GrYfCn7cn37+8/NzMu3HLXtduf+AuMeJe9zKuH9ohz8R2wHl3uO8hzYV7zpsPxT9uIOOvi+s/fsLiXucuMdtHffkLPjOw7ynhu0fjzqnDMl+3CELfnq2IxYS9zhxj9s+7re9SS3Dvvc776k7JgcYth/KftwJnxb8YrRuMCXuceIetzLud5tCviI6er/P+5kN2w9lP+69LfvpKe6TEPe4lXH/1A5/UfKe1du3cw4r21+T/bj3tPSn57pRnLjHiXvcXnEvOQseath+qMjHvfyn57phnLjHiXvcbnG/7U3ypqaA4Ybth4K7NO5mxU/PdT/UxD1O3ONWxv3XszreUmkW3G0bmePN/nGv+ukp7pMQ97hd415nFjzssP3QzHlf+QzadQv9xT1O3ON2jnuRWfAz98KOKXsP2YZW//QU90mIe9zecb/tzfTLsAcfth+a86fpskdsPbRuW0hxjxP3uJVxX/Qr9Nyz4CO2JhzLfBey35/w01PcJyHucSvj/vv52EeadweUSYbth+b6abpy2N78aF9lGXGPE/e4TNynHb3HH5C6kYnyfuJHvG5Dd3GPE/e4UNz/+edqwtnMVMP2QzexJzWdZv2wvRH3SYh7XCzu843ej951dkwz/LJ0yrC9Wbehu7jHiXvcyrivusg41WzmYsGCoEGN/nmfNmxvxH0S4h63Mu4rz7jmWcgx67D90Mif90aXqsV9EuIel437LLOZD1ft7U5v2M97s03Y2tdbRtzjxD0uHfcZlkVOPmx/ZMi8b3ipun3FZcQ9Ttzj8nEffRS8ySR4KMPlfdNN2FatChL3OHGP6xD3f/75MfBsZsablt40VN43vi9s1R9N3OPEPa5L3McdBZ+87HpU43zgSx6Qegxxn4O4x62M+8kNvP7cvtJINlh2Pa7vQ6yc2f6+MHGfg7jH9Yr7gKP3esP2x7qPw/a4VL3qZ5a4x4l7XL+4DzZ6n3KHsKWuu37i+/z0FPc5iHtcz7iPNAkuO2w/tG4nlk3s9dNT3Ocg7nF94z7K6H3iHcIW6jZ43+ympcdWTffEPU7c4zrHfYjR+8X4z77eTKdT9x1/eor7HMQ9rnvcu4/ez2LY/se6BxedaNeHWYn7HMQ9boC49x297zYuGFKPK6o7//QU9zmIe9zKuG/817Xb6P18hu33erR971+MVu1VJO5x4h63Mu7t6O10Gb1P9+zrE3Vo+/6rkMR9DuIeN0rcO4zez2vYfutn/Ml7iV+MxH0O4h43Ttzjo/dzO2//1v7cMZmtk1f9scQ9TtzjRop7fPT+tb3uWUhf1kjt5rBq/Y+4x4l73FBxj4/et96hcFzxB+7FZl7iPgdxjxss7unR++WZLIT8Gb6gEdzNQdznIO5xw8U9PHo/j8F7eNwe3TpZ3Ocg7nEDxj08eq8/eL/JjmTCWyf/aC+7iLjHiXvciHEPj96rD97DI5n0AtNVu+WIe5y4x40Z9+zovfbg/Wv7U2bkt04W9zmIe9yocY+O3jd9Gv9YsqtkeuzmIO5zEPe4ceMeHb1XHbz/O3lTap/nFF61V19E3OPEPW5d3C/b0TsLjt5LjmZukleme+3mcN1efxFxjxP3uKHjnhy9F3w8dvRKarfnFIr7HMQ9bvC4J0fvn4qdvCdP2ztunSzucxD3uOHjHhy9l7quevW+/akCuv7WI+5zEPe48eN++9c3Nnqvc/L+LXglte/WyeI+B3GPmyHuwdF7kcl7dgFke9FObtq7WETc48Q9bo64B0fvFZbNJE/bb7VX7aW9i0XEPU7c42aJe3D0Pvua9/juvu11e2nvYhFxjxP3uGnivnK4usbcF1bDp+232gv30t7FIuIeJ+5xE8V91d/idea9sBo/bb/VXrqX9i4WEfc4cY8T9+dNOpvJn7bfaq/dS3sXi4h7nLjHifsLZpzN9Dhtv9VevZc1l9rFPU7c48T9RZmH92+oy2n7rfbyvYj7FMQ9TtxfMdXoPXlL6qH2BnoR9ymIe9xMcU8tdX9gmrzfZJ/JcaC9hV7EfQriHifub5gj7+FH6R1q76EXcZ+CuMeJ+1sux184E923/an2LnoR9ymIe5y4v230hTPfO11I/a29jV7EfQriHifux7j80d7CgDqtf3ygvZFe1lxIFvc4cY8T9+OMOnq/+dr5tP1Weyu9rPnhJu5x4h4n7scaMu9dL6T+1t5LL+I+BXGPE/ejXQx3ZfX6Y3trfbV304u4T0Hc48R9gcGurA4wkbnX3k4v4j4FcY8T90Uur9p76W+Iicy99oZ6EfcpiHucuC80yOi9/xqZv9pb6kXcpyDucTPFvdvmKYe+9s/7CGtk/mpvqhdxn4K4x80U91FOVruP3ntt//iC9q56+Vd7G0uIe5y4x4n7Gl13A/45yG8wf7T31Yu4T0Hc48R9nW6j958jfQq/tHfWi7hPQdzjxH2tLqP3AdMu7hxD3OPEfbX86H3ItIs7xxD3OHE/QXTV+/XXMdMu7hxD3OPE/SSp0ftYax8fae+xF3GfgrjHifuJIhvOfB/mbtTntDfZi7hPQdzjxP1U+4/eBx21/9HeZi/iPgVxjxP3073fdTYz0kYDz2tvtBdxn4K4x80U9zV/izP2G73nhu0XH9b+FGlvtRdxn4K4x4n7JvaazcQej3rx5eaff67WvVp7r72I+xTEPU7cN7LHjgS5YXtb1Pmt/ddl7g/t53N7G0uIe5y4x4n7ZraezVx/al94fx9vT9vvrVqV047t5Ut7G0uIe5y4x4n7hrZcFhlc2X7xo73mulCKO0cQ9zhx39Ll30yeKPicpc+/T9tvXbf/bZF2bC/iPgVxjxP3bW0zmwkuf3x0rWDNz5R2aC/iPgVxjxP3jV2cPpu5+dq+VsDD0/Y7a65OtkN7EfcpiHvc+cT9+kfodPjUZZGx5Y/v3n14svPZ9/ZPlmiH9iLuUxD3uLOJ+8XtF0g9w+iU2Uxwr4GLb+01H7hq/2yJdmgv4j4FcY87m7h/uP8SqR241ub9Zs1YZKU/6x8PtH+4RDuyF3GfgrjHzRT3k8L38dfXSNVz3Wwm+OTrl266WvHTrx3Zi7hPQdzjZor7mr/Ff3xqX+Sf69DJ+/K8B598fb/bwLM+tn9jgXZkL+I+BXGPO5u4f2lfJDj6+LBoNnOVG7a/9s5WfDjtyF7EfQriHnc2cX948TD27IvjR+/JYfur2+Cs2F6mHdmLuE9B3OPOJu7f2xe5lxrNHJv36FP0XpzI3PvR/q0F2pG9iPsUxD3ubOL+6Gz1S6ymb+c9mva3ZkUr1kK2I3sR9ymIe9y5xv2f69j9rpdfX+9p8KalIzYmXrG7TDuyF3GfgrjHnU3cn9yLGXzq9Gt5Tz77+uU1Mg+0f3eBdmAv4j4FcY87m7g/l9eTvuAiL+T9Jpn2I+f/y99RO7AXcZ+CuMedTdyfPWe9XrGqe62naY3O2p/ZR+Z54s4exD3ubOLevsZjyVPnTwd5Daf9zWH7b8uX27cDexH3KYh73ExxX/eEz6Z9jSduTvqRsdCHP3etBvcHu7XkbtnlF5rbgb2I+xTEPW6muK/Zjva3u00hX5BbN3Pr8tPPm5ufn5Mn7cddR/1D3NmDuMedS9xffcfRy5pxi9K+ppXtwF7EfQriHifu95KzmaxlG9zcEnf2IO5x5xL39+1rvCQ6m8k5/jrqH8s/5XZgL+I+BXGPO5e4/3pWx2t+1JvNXP5of7YlxJ09iHucuP/1rVbel11H/eNnO/x47cBexH0K4h4n7g+Ums18XpV2cWcf4h53LnH/8yCm1+X2At7Z4uuofyzfOawd2Iu4T0Hc48T9kRLLIldcR/1D3NmDuMeJ+2PRW1Z3cbHq4dy/3bSvcrx2YC/iPgVxjxP3p+Yeva+8jvpX+zrHa8f1Iu5TEPe4dXF/5Wb+HS2/1PfX3+djH2Pi2czxj219SftCx2vH9SLuUxD3uHVx7/N/VC7ut8WYM++nDNt/W7zvTTuuF3GfgrjHifsLZpzNvP0QvWMs/rHWjutF3Kcg7nHi/qLZlkWePGxvxJ0diHvcucT9W/sai0w1el9709IT79sXPFo7rhdxn4K4x51L3L+3r7HM9ZpwdLH+pqUnFj9GpB3Xi7hPQdzjxP11c4ze328ybG/EnR2Ie5y4v2X82czFqpnTi8SdHYh7nLi/bexlkVtdR/1j8e8q7bhexH0K4h4n7kcYeTaz4bC9EXd2IO5x5xL3E4fSoy6L3OKmpcfEnR2Ie9xMcV++X+FfJ0dwxNH7xsP2RtzZgbjHifuxhlsWufmwvVn852zH9SLuUxD3OHE/3lij94+bD9sbcWcH4h4n7kuMM5vZY9jeiDs7EPc4cV9mjIdon/Y4jjeIOzsQ9zhxX2iA2cxew/bmW3uZo7XjehH3KYh73LnE/ap9jQ1cL76Fc1unP47jdYsfeNWO62XNT1txjxP3uJVx3/XU8SWnxH3TIPYcve84bG/EnR2Ie9zKuO988viC9uJrbPyG14wCtrDN4zheJ+7sQNzjxH2dLqP3ixWPHFlO3NmBuMeJ+1o/47OZfa+j/vGjvdzR2nG9iPsUxD1O3NfLjt633yHsBYu38GnH9SLuUxD3OHE/QXA2s/911D/EnR2Ie5y4nyS0W+SuNy09Ju7sQNzjxP1EgdnMzjctPSbu7EDc46aK+0V79RX2e8O7z2Y+hW8qEHd2IO5xU8X9hJPkPQO562wmOGxvrtorH60d14u4T0Hc484l7u0r7GS32UzipqXHFt8I3I7rRdynIO5x4r6JfWYz+zxp6S3izg7EPU7cN7LDbCZ7HfUPcWcH4h4n7pv5csL13mfEblp6TNzZgbjHrYz7hjvoLjB23DedzeSvo/4h7uxA3ONWxr1PegaP+3YXVi+TNy09Ju7sQNzjpor7+/bqK7SvsLfrz+31ThG+aemxm/Y2jtaO62XNs1PEPU7c46aK+wmPQGpfYX+nX1hN37T0RHsfR2uH9SLuUxD3OHHf3IkP8rhsX6af9kaO1g7rRdynIO5x4r69007exX0hcZ+CuMeJ+x5OOXkX94XEfQriHifuuzjh5F3cFxL3KYh7nLjv42b1shlxX0jcpyDucVPF/WN79RXaVwj61l55KXFfSNynIO5xU8X9hDtA21dIulq3HcFFO7yf9kaO1g7rRdynIO5x4r6flZdV29H9LL1e0A7rRdynIO5xK+P+vR2eNVncf7TXXqgd3Y+4sz1xjxP3/SzepOWXdnQ/4s72xD1O3HfUXnuhdnA/4s72xD1O3He0brF7O7ifyeK+5lMW9zhxjxP3HYl7grhPQdzjpor7Cfvptq+QtWZg0OspVw+IO9sT97ip4n7Cli3tK2St+01D3JcR9ymIe5y470jcE8R9CuIeJ+47Wvd+xX0ZcZ+CuMeJ+44mjfvSpxm2w3oR9ymIe5y47+h7e/Flusd96XXgdlgv4j4FcY9bGfcv7fAscY8Qd7Yn7nHivqOf7cWX6bMp2wPizvbEPW6quK87E77XvkKWuCes2VlZ3OPEPU7cd7Ru5zBxX6a9i0XEPU7c48R9R+Ke0N7FIuIeJ+5x4r4jcU9o72IRcY8T9zhx31N78WXEfZn2LhYR9zhxjxP3PbUXX+ZHO7gbcWd74h4n7ntas0qv0w1iD4g72xP3uJVx/9wOzxL3CHFne+IetzLun9rhWevWjd9rXyFszrh/bG/kWO2wXtq7WETc48Q9Ttz3tPQc+F73uC/dqbgd1kt7F4uIe5y4x4n7nsR9f6vWm4p7nLjHifuelg447on7IuI+B3GPE/c9rXoUU5+VSA+IO9sT9zhx35O470/c5yDuceK+J3Hfn7jPQdzjpor7VXv1FdpXCFu1Ab24LyLucxD3uJVx/9gOz1r19/iX9hXCxH1/4j4HcY9bGfcP7fAscY8Qd7Yn7nHivqdV+yX02drhgbnivmpWJ+5x4h4n7ntaFfc+1zMemCvuq66yi3ucuMeJ+57EfX/iPgdxjxP3Pf1or76IuC8i7nMQ9zhx39Oq8Ij7IuI+B3GPmyruqzYA/KV9gbA54/65vZFjtcM6Efc5iHvcyrhftsPD2quv0L5A2KqVHN3jvnQBZzusE3Gfg7jHifueVs2R+twg9oC4sz1xjzuTuF+0LxC2Ku6dRl5/zRX3VRetxT1O3OPmivvPVU+/uNXp/Yr7/lYtNxX3OHGPmyvut3lf9VjSbu+3vfwi4r6IuM9B3ONmi/vtX+Y1eRf3BcSd7Yl73HxxX5X39+3QtPbyi4j7Iqvi/j/bwcSIe9yMcV+R917BvGivv4S4LyLucxD3uJVx7/7/1MK89wrmTCOkP8Sd7Yl73KxxX5j3XmvHp4z70ly2wzr51t7FIuIeJ+5x88Z9Ud573fUp7rtb+nvGPXGPE/e4meO+IO+9HoDxvr3+EuK+iLjPQdzj5o770Xnv9ei6NTddifsi4j4HcY+bPe5H5v17+5fTxH134j4HcY+bP+7/XH95O+8/27+btibunfbB+Uvc2Z64x62N+3U7fgjXbz5e4qr9m2lLn3txrx3bjbizPXGPKxH3t/N+0/69NHHf3dJHi9wT9zhxjysS9zfy3m3SMWXcl+6Q3g7rZNVHLO5x4h5XJu63eX95wN3tjn5x3524z0Hc4wrF/Z9/frx0ZbXXSsh1M4N2bDfizvbEPa5U3F9cF9lrscy6q33t2G7Ene2Je1yxuD+f936LC1fFvfdnK+5sT9zj1sa927nw257mvdfOMuIe8LG9i0XEPU7c4wrG/Wne+9Vy1ZaFveO+9Mmv7bBOVj1WV9zjxD2uZNxv8/7wr3y3y6m376O9hUXEfQlxn4O4xxWN+z//XP3r9+n7v9r/0oO4707c5yDucWv2pL0zfNxv/fzy4f37T13fqbjvTtznIO5x/7N9ty81Q9wHsCruvTbC+U3c2Z64x62Ne689dCezdOHJvd4/OG/a+zhWO6yTVb97inucuMeJ+66mjPs/7X0cqx3VyfO3rb1B3OPEPU7cdyXuuxP3OYh7nLjvStx3J+5zEPc4cd/V0muT98R9CXGfg7jHifuu5oz7RXsjR2pHdSLucxD3uLVx/9KO51Wr4t79B+fCXrajOmlvYhlxjxP3OHHflbjvrr2JZcQ9TtzjxH1XS5eM3xP3JdqbWEbc48Q9Ttz31T6uRcR9ifYmlhH3OHGPE/d9tY9rke5xX3hHfzuqj1W/G4l7nrjHrY3753Y8r2sf1yLivsCqqxrinifucWvj3u/hRnNpH9ci4r6AuE9C3OPEfV9rVmF3H3mJO5sT9zhx39eUcV/4zOl2VB9X7U0s87/a0cSIe5y470vcd7Zq9x5xzxP3OHHfl7jvTNwnIe5xa+P+oR3P68R9Z+I+CXGPE/d9rXkIXPe4f2lv5EjtqD5+tDexjLjHiXucuO9rTdy730MwU9xXPaVW3PPEPU7c97Um7t2vZ4g7mxP3OHHf15RxXxjMdlQf4j4JcY9bG/fLdjyv+9g+ryXEfYFv7U0sI+5x4h4n7vtauPDknrgvsHCE1Ih7nLjHifu+poz7whUo7ag+xH0S4h4n7vuaMu4L1463o/oQ90mIe5y472tN3LtfrJ4p7p/bm1hG3OPEPW5t3P1fdZw17eke94U7Lbaj+ljz01PcO1CMOHHf15qpgbgvIO6TUIy4/6t9ty/Wjud19eP+/mc7qg9xn4RixP2v9t2+WDue100Z9wXPj7r41g7pZc1dYuLegWLErY77TfsCvKp23C++dP82EPdJiHvc6rhfty/Aq9bcQPm+HdvPRXsnb/g0wE94cZ+EuMeJ+77WbH3Sf5npUbvQf+g7bG/EfRLiHifu+yob98sh0v7PP+/b+1nmf7ejiRH3OHHf15xxf/N0uPt11D/WPOpK3DsQ97jVcb9qX4BXzRn3N/ayHOA66h/iPglxj1sd90F+Kx/dnHF/ffH4p5F+axP3SYh7nLjva83zm8eO+xjXUf84cmHPI+IeJ+5x4r6vVQ/nb8f28/Lq/FGuo/7R3tdC4h4n7nGr4/6jfQFeNWfcX1qdP8511D/aO1tI3OPEPW513L+3L8Cr5oz7C1cKPg94W3J7awuJe5y4x4n7vuaM+7OPYhps2P7LTXtzC4l7nLjHifu+Fu6e+0s7tp+r9kYeGG7Y/suqz1fcOxD3uP+7fbcvNt70dUhzxv3Jux5w2P6LuM9C3OP+d/tuX+xL+wK8alV8uo+2Hw07Rrpp6RFxn4W4x4n7vlbFp/9NQg9Xjw+c9pXXNMS9A3GPE/d9TRr3v9txDZ321XH/P+1wYsQ9bnXcP7cvwKsmjfvvW1Q//Lv9D6MS91mIe9zquH9qX4DXtY9rkf5x/3z3Ni4+j38bsrjPQtzjxH1n7eNapH/cf767+PB96HlM8+yK/LeJe5y4x4n7ztrHtUj/uP8zwFs4yppdN2+Je5y4x62Oe/+nOM+hfVyL2Cv/aOI+C3GPE/edtY9rETtuHk3cZyHuceK+szX7jYv70V7enPhV4h4n7nH/p323L/a+fQFet+ZJQeJ+NHGfhbjHrY57/8cFzUHcdyXusxD3OHHfmbjvamXc/592ODHiHifuO1sTd0+5Otrrj/J+0X+0w4kR97jVcff/1XHWxN1e+UcT91kIRpy47+zvFlzHE/ejifssBCNO3Hf2oX1cS4j70cR9FoIRtz7us9yg3pm472rNx3tL3OPEPe4/2nf7cuJ+FHHflbjPQtzjxH1n4r4rcZ+FuMetj7vF2EdZUx8PHz/ayrj/v+1wYsQ9Ttx3tuaKn0cYHm3NYqRb/9kOJ0bc49bH3Z02RxH3Xa25jeCWuMeJe9z6uBsMH0XcdyXusxD3uP9s3+3LiftRxH1X4j4LcY9bH3dX/Y4i7rtqn9hS4h4n7nHr465ARxH3XbVPbClxjxP3OHHf2ef2cS3h4eNHa5/YUv/VDidG3OPWx12BjrJmw3Ef7dHaJ7bUf7fDiRH3uP9u3+3LKdBRxH1P1+0TW0rc48Q9bn3cP7avwKvEfU/iPg1xj1sf9w/tK/Aqcd+TuE9D3OPWx/19+wq8Stz3JO7TEPe89t2+nIeoHuVb+7iWMPE61s/2iS3VDifHZ57XvtuXu2hfgFd9bx/XEiZexxL3afjM89p3+wrtC/Aqcd+TuE/DZ5530b7dl7tpX4HXiPuexH0aPvO8lTsv3fIopmOI+57WfLp32uHk+Mzz1sf9qn0FXiPue1oZd4sB8sQ9b33cPa3jGOK+J3GfhrjnrXwI5S0buh/jR/u0ltCeY4n7NMQ9b33cbeh+jDWX/LTnWGtuEbvlA84T97yP7ft9uc/tK/Aacd/Tyribe+WJe96ah0n84ib5Y4j7nsR9GuKetz7uNpc5hrjvSdynIe55K/963JKgY4j7nlaemoh7nrjnrY+7W1SPcdU+rCXE/Vgr426imCfueSsXk9352b4Er1i1KW07lreI+zR8U+edEHfLZY4g7ntaudZL3PN8U+etucmmuTCXeZu472nlXRpf2uHk+KbOWzMT/s3fkbeJ+57EfRq+qfPWPqjszoWNId8k7nsS92n4ps67ad/vq1hS9iZx39PKuNsWKc83dQft+30d+8u8ZdUPTxczjnK1dmMkcc8T9w7W7/l7x6bub2kf1CLGXUe4+do+reXsVp0n7h28b9/w61zq0BvaB7WID/VtP084K3GHRp64d7B+W8h76v6G9jkt4jN9y/Xaicw9cc8T9w4+t2/4tdT9de1jWsRH+oav65/rfsfnmyfuHXxr3/Crqfur2qe0iE/0VT9PGyX6fHsQ9w5O2H+gUffXtA9pER/oK25O/V3TUtMefOgdnHKLanNhhvmyNQMEcX/Zt9MmMvfalyLIh97BSXcx/Wa9+4vWLOrww/Ilq5e2P2RL5Q7EvYfTFro3doh8ibhvZ4OJzB3PEOtA3Hs4cS1k894o4XnivplTlrY/ZNeMDsS9h23Oht5duu3vWeK+kdOWtj/0sX1FgsS9h9OXyzRf2xfkIXHfxolL2x8yQ+xA3Hs4ZdPfQ9ZEPkPct7DVROaeHX87EPcutjsluvh3+5L8saZKJlyHbj61D2YbNoXsQNy7WPmU4Wd9cvL+yJq7KdXnwBZL2x/yi1EH4t7FZkP3O5dO3g+tuQwo7g+cvNnAE/ap7kDcu9jkNqa/nLwfEPeTXG+zUveAZ6F0IO59bLbG7Bcn7w+J+yk2XCPzx0X72iSJex8/2nf9Zpy8/yXu6226RuYP9zD1IO593Gx+euTk/Q9xX2u7u5YOuYepB3Hv5Ev7tt+Qk/dmTaHsw3Z7xrHHROaeZe49iHsn25+63568K9S9NRcE5Wenicw9dxH0IO69bLnU/Q83rN5Z89Gefdz3msjc823Zg7j3st0WBAfsNiPuK+w3kbljN/cuxL2bjbaGfMyFVXFf7Md+E5k7n9rLECXu3ewxdb939hdWxX2ZTZ619Boj9y7EvZ8dFsz8cnHmF1bFfYmNnrX0ClOZPsS9n91O3c/9wuqauJ/t5GDrLcKeYSrTh7h3tNup+61zns2I+9F+7j2RuWUJVyfi3tGOp+5nfWFV3I+08abtL3CFvxNx72nPU/czPmNa87GeY9wDE5lbhjK9iHtPu5663zrT2Yy4H2PHG1IfMpTpRty72vfU/VxnM+L+tl1vSH3gQtu7EfeubnY/ezrHEydxf8u+N6Q+5BFM/Yh7Xz/b34Ednd9sZk3cz2rH8dBE5padlDsS9852v4Pk9uT93PabEfdXpSYy795duDW1J3HvLfE37cxG7+L+ipuv7U+8v0szma7Evbf9x+53zmo2I+4v+54atr+7+OKp2H2Je3fXmQHo1/P5q/a9/ZGXOI+4J25Ivff+m7J3J+79hep+PrMZcX/e/luENR9+tlekJ3EfwPX79pdiZ+/PZDazJu5nsHNh5oZUaR+GuI/gZs1TP9c4j9G7uD/jZ+gMQtqHIe5jWHMNcI2zeIa2uD9xHTp9uJT2cYj7IGKrGM5g9C7uj6RuSL2wAeRIxH0Uocuqt8rPZsT9UOiGVGsfByPu40itZSifd3F/KHVD6mdpH4y4D+R77OS99mxmTdwv2rHVpJY/fjinu+QmIe4juV7zDKF1Lgtv+/Gj/RkXaccWE1r+aInMiMR9LLmT98KzmVVbbbZjSwndkHpxDkuwJiTug8lt2ffu3eeieRf3e5knpLqOOixxH07w5L3o6F3c74SWPxq2D0vcxxOcvNccvYv77WeQOUVw09LAxH1EwZP3iqP3VXEvNVsIDfcM24cm7kNKnrzXG72vinuhDyF1Q6qV7WMT90HlHnNZb/R+5nH/kfnWsfxxdOI+rNReYndq5f2s4275I424jyu5KrLU6P2q/ZkWqfHnT92QaiIzAXEfWfLCaqG8X7c/0SIl/vRuSOUvcR9a9MJqmb3ezzXuoedxmMhMQtwHl9sJ+FaR0fuquE9/Mpp6HoeJzCzEfXjR2UyJ+w3PMe6p5Y8frtoLMjxxH190NlNh9H6GcU/dkFp4L9F6xH0G0dnMxdf2qtM6u7inbki1RdhUxH0O0dnM7KP3M4u753HwLHGfRHY2czn13+NVcZ924BBa/vh++ivOZ0fcpxGdzUw9el8V9+/t4Mm4IZWXiPtEorOZd1+nzfv5xD30PA7LH6ck7jO5Dk1Xf5l39N7+AItMGPfY8kcTmSmJ+1zCo/dJ897e/iLzxd3yR14l7rPJzmbmHL23N7/IbHG3/JE3iPt8vmR+GW9mzHt764vMdcXw5mt72zv7OPWyqTMn7hMKz2bmu6mpvfNFvrRjp/DdsJ03ifuUrqKzmelG7+19LzJR3C1/5BjiPqns6H2ym5rau15kmrhb/shxxH1W2WWRc43e23teZJa4p5Y/GrZPT9znlR29z3RTU3vHi8wR99TyR8P2AsR9ZuHZzDSj9/aGF/nUjh1ZbPljez2mJu5z+ybvz2hvd5Hx4566IdWwvQhxn1x4NjPHuuc1ERw+7pY/soy4Ty/0u/pvM1xZXfPrzOBxDy1/tNdAIeJeQHb0PsGTmsrFPbT80V4DpYh7CV+M3h+qFvfQsN1eA7WIew3h0fvgS+XWxP1DO3Y8oeWPhu3ViHsV1+/bX9KMoUfvleKeWv5or4FyxL2O7Oh95LzXiXto90fD9orEvZLsZsCXw57slYl7avmjYXtF4l5KevQ+6JXVInEPLX98b9hek7gXEx69vx/ynG9N3C/bscNILX80bK9K3Msxeq8Qd3sNcCpxLyg7eh9wu8j5455a/mjYXpi4V3Tuo/fZ4x5a/mhj39rEvabw6H2wvM8d95vMY1hs7FuduFcVHr0Pdev6mp9sF+3Y7r4ZtrMJca8ru+HMSFdWV0012rGdhZY/2mvgDIh7YenR+zDbRU4b9+uP7c3sy7D9LIh7aeG93kcZvU8a99DyR3sNnAlxLy48eh/jnHDOuP/I/F/1SdrPhLiXl33M6hCj9xnjbtjOxsS9vvDo/d3n7nlf1cmu7zq0/HH4x6ywIXE/B6HrdL91T8h0cTdsZ3vifh7So/e+eZ8s7qG9BoZ+vgrbE/dzEc57111Lpor7lWE7uxD3s5EevXc8UZwo7qm9Bgzbz4+4n5F03r/2GvGuivtVOzgqs9eAYftZEvezElpK/Vuv0fuquHcYW4SG7UPt+0OMuJ+Z9JXVH+11o+aIe+j2YcP2cyXu5+b6S/tLH9Jj9D5D3A3b2Zm4n5/6V1YniLuV7exN3M/RVXg2k94uclXcv7eDEzxFj/2J+3mqfVPT4HG3jQwJ4n6uwqP3y+RJ5NBxD6X94lt7Pc6VuJ+twqP3VX+yTNxTaTdsR9zPWPgp2u++pvI+bNxTO7gZtiPuZ67o6H1V3L+0g/cTetLSu/eG7dwS9zMXfop2Ju9jxj20RMawnV/E/dylR++Je+FHjHvqcbaG7TTiTr0rq+PF/eZre5WdGbbzh7gTH71f7H1T03BxD23YZtjOA+LOnVpXVlfF/VM7eHuhiYxhOwfEnXvXmX2s/rjc8yRzqLin1sgYtnNI3GkKjd5HirttZOhE3Pkj1KE/Pu8VpHHiHprIGLbzlLjzQJHR+zBxD23sa9jOM8Sdh27S+4ntkvdVcf/QDt7Oz8z2DobtPEvcOZQeve8xLB4i7qFHLRm28wJx57H0fmLbX1kdIe7fIhMZw3ZeJO48NftNTf3jnrmQatjOK8Sd58y9n9iquF+2gzcQ2mzAsJ3XiDvPSo/eN72pqXPcM5sNGLbzOnHnBakHS/y24ei9a9wzExnDdt4i7rwoPHrf7qamjnHPTGQM23mbuPOKSW9q6hf3zETGsJ0jiDuvie8ntkne110vaAefIDORMWznKOLO62Z8UlOfuGcmMobtHEnceUt6P7HTr6x2iXtkImPYztHEnbelR++n3tTUIe5XkYmMYTvHE3eOMNl+YuvifsIvDJl9ZAzbWULcOUp69P7+lJCl4x7ZR8awnWXEnSNNtJ9YNu4/ExMZw3aWEneOlr6p6evaCXMy7pmJjGE7i4k7C0yyn9i6jROu2tGLRCYyhu2sIO4sMcd+YuvmJCteKvKsJcN2VhF3lknnfc3oPRT3yNZqhu2sJO4sFR+9L857JO43iadfXxi2s5a4s9zo+4mti/v3dvRxIvftGraznrizQnw/sWVn1fvHPXJD6gfDdk4g7qwy9Oh977hHlj8atnMacWel9H5iCx7lsXPcE8sfDds5lbiz2rCj93Vx/9KOfoNhO3MQd06Q3k/sR3vdN+wY98jzOAzb2YC4c4oxR++7xT3yPI6LTZ5GxdkTd04z4n5i697S53b0ywzbmYi4c6rxRu/r3tCndvRLIsP2LZ4yCHfEndONtp/YHnG3sp3JiDsbSI/e3zi/3T7umZXthu1sSNzZxFBXVtfF/UM7+inbyDAhcWcj6f3EXsn7xnGP7Nm+6BZceJu4s5lh8t7++ULv29GPZG5aMmxna+LOdq7TNzW9sBtw+8cLXbajD0QekLr2iVPwCnFnS+nR+/N5b/9woWfinnn2tWE7exB3tnUVns08M5y5af9koYt2+B/Xn9o/2dUnaWcX4s7W0qP3J3m/bv/7Uu3wJpN2w3b2Iu5s70tieclDHw8SedX+16Xa4fciqx+P3gkNlhN3dpAevd9dk/x7+v6z/W9L/f0KmbN2w3b2JO7sIrI17qFPv0/ff7T/YanfcY9cRn337rO0sydxZyfx0fvt6funH3fBXHvaff/D4eZHJu2G7exM3NlNh7y/e3fxYfWrfr89af+cuVyw8InfsJy4s5/86P00l6ELwRdHPtEPTiDu7Gm2vEcYtpMg7uzrR4/ZzMg+XLVPBnYl7uyty+h9VIbtpIg7u0vvJzaui2/tI4HdiTsBRu/3DNsJEnci4vuJjefDkx3OYEfiTsiZj97fG7aTJe7EnPHo3bCdOHEn51xH73YIowNxJ+n6fevdOTFspwdxJ+vsRu92CKMPcSftyznl/cKzr+lE3Ik7n9G7YTv9iDsddHiURw9Pn90NMeJOF2cwejdspytxp5NvtfPu2dd0Ju70Unn0bthOd+JOP9cfWwursUMY/Yk7PZUcvRu2MwJxp69yefc4DsYg7nRWa/Tu2deMQtzprlDeDdsZhrgzgCKzGTuEMRBxZwgF8u5xHAxF3BnD9efWyEl5HAeDEXdGMfPo3U1LDEfcGcePWWczhu2MR9wZyZSjdzctMSJxZyjzjd4vPY6DIYk7g5lr9G7YzqjEneFMNHr/JO2MStwZ0CSjd8N2BibujGiG0bsdwhiauDOm0Ufvhu0MTtwZ1dCjdzuEMTpxZ1zDjt7dtMT4xJ2BjTl6t0MYMxB3hjbe6N0OYcxB3BncWKN311GZhbgzvIFG7x8N25mFuDO+UWYzblpiIuLODEbIux3CmIq4M4fesxnDdiYj7syia97dtMRsxJ1p9JvNGLYzH3FnItcfW22j7BDGjMSdqeRnM25aYk7izmS+RfPuOiqzEndmkxy92yGMaYk787n+0Nq7M9dRmZi4M6PE6N1NS0xN3JnTl4vW4J0YtjM5cWdS+47e3bTE7MSdaV3vNpv5cNVeAqYl7kxsn9G7Jy1Rgbgzs5svLcjbcdMSNYg7c9t49O46KlWIO7Pb8jl8n9y0RBXizvy2Gr27aYlCxJ0CNpnN2PyRUsSdEq7ft0Sv5ToqxYg7RZw0m3EdlXLEnSpOWBb50XVUyhF36lg5encdlYrEnUpWLIt0HZWaxJ1aFo7eL76046AYcaeY6wWjd9dRqUvcKefo0bv7USlM3CnoqLy7jkpp4k5JP996zKrrqBQn7hT16pVV96NSnrhT1ot5dx2VMyDuFPZ83j0flXMg7pT248ns/YMlMpwFcae4nwcrZyyR4VyIO+Vd/+v3dEbaOR/izjn48a8Laee8iDtn4r/+q/0HOAviDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAMUJO4ABYk7QEHiDlCQuAOU888//z/T7BQkdnV4JgAAAABJRU5ErkJggg==";
        else return image;
    }

    public boolean isOnline() {
        return this.status != Status.OFFLINE;
    }

    public String getStatusString() {
        switch(this.status) {
            case OFFLINE:
                return "Fuera de línea";
            case BUSY:
                return "Ocupado";
            case AWAY:
                return "Ausente";
            default:
                return "En línea";
        }
    }

    public int getStatusColor() {
        switch(this.status) {
            case OFFLINE:
                return new Color(77, 77, 77).getRGB();
            case BUSY:
                return new Color(255, 62, 62).getRGB();
            case AWAY:
                return new Color(255, 183, 62).getRGB();
            default:
                return new Color(83, 255, 62).getRGB();
        }
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void removeFriend() {
        if (!this.getUsername().equals(GameBridge.getChocomint().getUser().getUsername()) && GameBridge.getChocomint().getFriendsManager().friendsPool.containsKey(this.username)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", GameBridge.getChocomint().getConfig().getToken());
                jsonObject.put("username", username);
            } catch (Exception e) {
                GameBridge.getChocomint().getNotificationManager().addNotification(new Notification(
                        Notification.Priority.ALERT, "Error", e.getMessage(), a -> {
                }
                ));
            }
            GameBridge.getChocomint().getRestUtils().sendJsonRequest("/api/deleteFriend", jsonObject, res -> {
                JsonObject response = new GsonBuilder().create().fromJson(res, JsonObject.class);
                if (response.get("code").getAsInt() == 200) {
                    GameBridge.getChocomint().getFriendsManager().friendsPool.remove(this.username);
                    GameBridge.getChocomint().getAlmendra().deleteFriendPacket(this.username);
                    GameBridge.getChocomint().getNotificationManager().addNotification(new Notification(
                            Notification.Priority.SOCIAL, "Amigo eliminado", username + " ahora ya no es tu amigo", a -> {
                    }
                    ));
                }
            });
        }
    }

    public void acceptFriendRequest() {
        if (!this.getUsername().equals(GameBridge.getChocomint().getUser().getUsername()) && GameBridge.getChocomint().getFriendsManager().friendRequestPool.containsKey(this.username)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", GameBridge.getChocomint().getConfig().getToken());
                jsonObject.put("username", username);
            } catch (Exception e) {
                GameBridge.getChocomint().getNotificationManager().addNotification(new Notification(
                        Notification.Priority.ALERT, "Error", e.getMessage(), a -> {
                }
                ));
            }
            GameBridge.getChocomint().getRestUtils().sendJsonRequest("/api/acceptFriendRequest", jsonObject, res -> {
                JsonObject response = new GsonBuilder().create().fromJson(res, JsonObject.class);
                if (response.get("code").getAsInt() == 200) {
                    GameBridge.getChocomint().getFriendsManager().friendRequestPool.remove(this.username);
                    GameBridge.getChocomint().getFriendsManager().friendsPool.put(this.username, this);
                    GameBridge.getChocomint().getAlmendra().acceptFriendRequestPacket(this.username);
                    GameBridge.getChocomint().getNotificationManager().addNotification(new Notification(
                            Notification.Priority.SOCIAL, "Solicitud aceptada", username + " ahora es tu amigo!", a -> {
                    }
                    ));
                }
            });
        }
    }

    public void sendFriendRequest() {
        if (!this.getUsername().equals(GameBridge.getChocomint().getUser().getUsername()) && !GameBridge.getChocomint().getFriendsManager().friendRequestPool.containsKey(this.username)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", GameBridge.getChocomint().getConfig().getToken());
                jsonObject.put("username", username);
            } catch (Exception e) {
                GameBridge.getChocomint().getNotificationManager().addNotification(new Notification(
                        Notification.Priority.ALERT, "Error", e.getMessage(), a -> {
                }
                ));
            }
            GameBridge.getChocomint().getRestUtils().sendJsonRequest("/api/sendFriendRequest", jsonObject, res -> {
                JsonObject response = new GsonBuilder().create().fromJson(res, JsonObject.class);
                if (response.get("code").getAsInt() == 200) {
                    GameBridge.getChocomint().getAlmendra().sendFriendRequestPacket(this.username);
                    GameBridge.getChocomint().getNotificationManager().addNotification(new Notification(
                            Notification.Priority.SOCIAL, "Solicitud enviada", "Se ha enviado la solicitud a " + username, a -> {
                    }
                    ));
                }
            });
        }
    }
}
