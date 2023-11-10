package com.tiantian.yy.ui.other;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;

import com.tiantian.yy.bean.VersionBean;
import com.tiantian.yy.dialog.MyDialog;
import com.tiantian.yy.netUtls.Api;
import com.tiantian.yy.netUtls.HttpManager;
import com.tiantian.yy.netUtls.MyObserver;
import com.tiantian.yy.utils.LogUtils;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tiantian.yy.R;
import com.tiantian.yy.base.MyBaseActivity;
import com.tiantian.yy.utils.ActivityCollector;
import com.tiantian.yy.utils.Const;
import com.tiantian.yy.utils.MyUtils;
import com.tiantian.yy.utils.SharedPreferenceUtils;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;


import java.lang.ref.WeakReference;
import java.util.HashMap;

import cn.sinata.xldutils.utils.StringUtils;
import cn.sinata.xldutils.utils.Utils;
import io.reactivex.functions.Consumer;

/**
 * 启动页面
 * Created by Administrator on 2018/10/10.
 */

public class LancherActivity extends MyBaseActivity {
    MyHandler myHandler;

    @Override
    public void initData() {
//        initClinkApiCall();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_lancher);
    }

    @Override
    public void initView() {
        showHeader(false);
        showTitle(false);
        setRxpermiss();
    }

    /**
     * 初始化clinkapicall
     */
    cn.ay.clinkapi.Api clinkApi = null;
    private void initClinkApiCall(){
        String key = "ck0AAJ+TUbloWxEAAF44vGZsrcLvtS3BbvK5YUJCbDhNyC8TV8okfVN035PIUzM2beAK8yzOpIqDSnOr2e92JuapuTIuJGWbRzBve9SY9xu+gvNgN3gMfSSkjwxWeKNj/LmsHpgBB0l6bqv6Xyi06VFY7Fb7Fu1unTsNiZRYRM6MDH0b69mBvHup5MjLEB767qS6/YpMZubKPoBc8n20ISncbVOpmf131i/ZUTAloYwdEZnunUZDP6SWT6xYJMj99MOe7qLJUFWmQyZbchW1pJvzPuHixYPimC2uE0lQHrFl21tshbHpU79ER1kf58yO36kQZOTmGy8/u1Z0Gz0MVSVz9JW5SR7oCeohnX0qwq9gHzOeP7nMfcArLMlbdTeCN/A0qvE21JWKJ1r4xXyxFfilE/oJbRZrpnfUjRxUjZOynWaL9Re9KUqcExhe0rZEeRfQtWE8eMnaxwR6YUd4NpnFRSgVJR/g5uivbFn8DNO/kBMFg9YydiSbsC0TQscDI9x+l4yOLlP37s13iwxv3LowutAzc7LBk84DeXPwzaQhrEh8fnRGMGG+DJMIuekYK9Nx4of1z64pu7rUU8NYuoxGSbviYfpe3njqzGwh9LWc2OLFG+RHhqOtFFtvE9cAxpwnsEMsyOwboeUvWSU5ZryHT/hvzpGNEgxOsp8ejrJfo+zArk4MRhIju4NSB/0RWjuwVcw5B2QulWkhybff0CsOhKNDK1mBNUi8RwP/Szvcqd9UKNKDVJtPZ537MneyAn3WOzl0WTWspcc+weoyakwA4RBCxL8Yf79EejArrwaZfw15pFcIq7vx3C6ToeB1FZIpU66nt4uKHivKYk9H9lsCLh6Zq74uZQMHU2gwH/Y8v4mOC9o4HZDFZd0MxKFCNUzuexCSMKBl7CdY7bQ67xJw04Vc68JSuT8pXBOaI+igZPxDx/pKLSVSbZTEc5Oe9Q/Y4/qzzuu84cPbnwlrY29rbaGxhC3wkTpAxzxmcd6fthxDRUSC9LDEWmRF6CReBhm5ACpHmxWlHoc1O7n0Gfe0NhIu5eVODvpYT13h8UnCC1JLhvt3mBl18AfzQOHe89lgbLR03wxzgdi+Ze0LkGe7DTDdmYYMdqN1Cz7+BVIUUTBgRGIsQzA70SkCvGSW55Lt70dyXjhnVdVtygl9wgWNvcVS04cNvGMe7r8se9gGPtzJEjX4wCQiOzb4Vmdoi6NyTXO8j2CQ8v7flnId/TNCeCZ7anQbVi+TSq1JCMX6jHYFLeYa2VYYDsXWeAyBAA/wgKHmFs1PryQ7+WA8TIu3rAZSXTjZ1aZkPMu3ExNhl91tufq0gVkhQc7BCa/m6vETANosmWeEwqRLEils6NAdPgm9kxukHL7KoI+NTuAhCG0tuMrbHAiZSkuBNqpz513wH+7GWP7K0nEDRMzLHt+Q3TKjX5WluGoqDeYEFVFBu+BbdvOC47JzBGNzSiQGc7c73bqMegAhT2xj0KCgr1kkVvv2DbdVWMOiAUsmnVWGN7j5Pb6GZGMeUJqfropQwIG1It07+QKX7u7YUljKZYCy1C4J2jFkdo7kYvJN/mS/QxRB05toDHzmT/j0mfS6wNEaaUDWuvi7HLe8Uryf+gFrO3LwfdVscrRTxEXaZ+zcrKOMcM7ZPENyakxo5JH0JgKfjJAr4uM6QCAcYNuOVinaPz6jihMnyweUx3Y6813fZvMe0PdvNYIwAloZcp++XT0YFprGZiVMdCcDMLzBgCFyureRb+Vx7DY3vptdNLeicQtAYkOSKW1CRy3IItyKEQYbjjAuEeA/I8JgYUKEC5w8o726R3tGk1jB1FHIeGTbqrNsDJjKELR1X28XzS0iLsYIqKSxf4x8/wcnpGNwMtLsTTo+z6XJNQX64Nyl89maWRy0+lQfjdvN+TgFDJumOVd5TWjirM01rZ2FsQmNCINNAWTYeFbVGTaI6/VHUkMivfUL22jd2fUI88MCxFu2xujDSrUeKqwc5X95I1Y2cHGFR27QUfRPY4E+9K1jVprPKUZtaKRoMCzdMiogxT9EBC4tYDJI6Y2IJiuf9GFfgMLyueRHzlZAQaffueeZGzUJLeY980nR6g9wI2hEcIqe45f+4BKb9WvfhOX0WBReS7cnvEAI0mWmblU88GIpu3dNumCVfxYvUuFjYoaUsumIfYuzae1POO1U9f4mrbnIgkg37wllzJl8cyuQFHt12/uc/8dVwfmsOx6NjYeh3CtWEedToISgeHn5sjP+1Apv2aB+9m+Yru2WlFGlUHmfAqbt5jFQmEkcShQm23i3qL40dJGCKtwbdeauVIZzUSsTyCnzmD12C7uiRoS40E9cT98nTb4nsSwhMelB91Q2AgYAGZ2EKmD1B/GeDHnHCRLI1MwOLE1QSopO+gXm6DQ06qcmXmwpBqU6aNsJZ8ObTEq71MxsnUyJjI2MyDMB8TpHAbCwv8eZ9D1aCpIbTM/nq7FLtKwtPv37apfbz9h0UECDtt32049XXSGsOY1PerrJ6KLtiokEEVi/y41Y85UsqMPVyllPJr6E4XP6nv7OUNv0G2SWni8qAx4q90G0W1tcLtXxlp8raLeDXcoFlGMn1/8ug/T7O0oj918Ryqor0Bpn0yjRjLk+xNiP0PfEz0/2wjudrVE418w/bJIqa2akVvXxiEFS1+W0aQx6DolISya0ioGgZPPm4Wbkdter6cLIfnRLfJb1lqYjitB+tRvjVagvaCbszWgBpoe5NYgu16sUmoxKF84+pUyYOOhKe3kUMI6fKXn1m/8to+VXjSDVTQRbb2TR9Eu1dqaLNnN0+DHHjAKibAQ+p0Jp+iBzLuK3Zel9jut0/+USj7Ct3gJvfmJgPullZPYnSSoZpKcp++lyoQS9AsNlZwIltu6S6ao66HagXSztLGWWmbulKJYZL95tXPfOV8D/U20UGEOpFMeFD3IM96l33lNUJUSQC8ClJZTRTH+qXZwjz7o75qFkUdU6/54CLji7TOj1nUR2atCY4qNeZ+5MS32qeLvXFCo3DYQCShnGkYr1+q5YTI0Iqo/iWYf0Zfak/kBB8EKEzeRwjs4pRwI6XwhzLXNHG97w96l2hGZTMUSG1/szZWc7JbE8N3zIPsDW3DsytQl+BGaYyatJJbcm7buZA/X/ZMgaLSWmberasPeTiLYKW+WMoQCuFtuc1Ec5GktJPnEIQhVK7KtM9tnOVN1Bf0KX2aGSdhefvHPghj6WHWG0BVoUzaegnKwIs+w9l92TcRFiO7fjMDHSJ74UsBc5vsT40Kj/EjFUTkke5YXwf96hXakJJGc/Y1rUGJOZqC9wjCTf4pe/FL1IlYfRJNq45d3wp9H6w68bPxxLOR9KtgJk4i0TLenXQcfsiVeiPqN62EEtO6fW9MY0b1l7mPIL9nLRzJ+/5g5EX9E/rhWswFOPXJVIOU8kfw++ny1b9mhomVmq+2m0VRG9x8BhtXZpuT6wcUm280rHkp6cj8t0NQ/V2A0+UdsHnNszr7fx7JrLpnmkcs3ivOEUXR2OooEs75Iq6EXgWi3DKT9wsvr/rl21yMM2U/pUxvLfbOU9F46n15oL51ewDPQrmtPDOylyg2MLtkPP/QsvCZlVBgyrod7zCHxDHI2E0gUrEoUlFi2U4bWVNRTSPpH4lzZGGMXKcTl+B+6EXChQqFkge+9mQCdxmnUUT0nhXT8stspp820Wdmoe1CqTEmeVvYBakJP6QmMCvjlvNrdyZWacws/25wAYJJ0BUBdVBqt4CKa8wUESRd4Go67f5mLinbxZk9T4wl77k/n4sdu9KHT7k68j8GbFTb9o1mGwo6E1v7a8HtKDTTH+atnTOlh2xNtST6ytvXCeT30oEDz935H5TpEBxr1WXS/E+ZMg0k8rJhnWH5n6aSY7eWshYi0Pr+wmlg8KLkqnxqwTZECDLkCEsHO4OONZFev68ZEBtwQZPrFfDomf8Trg9idmUZ7aY+fozflJuHuHP8mMjEl66cAZAUlYp0VLCRWYIG3UwRdWjVWP1gecO7U1HdEcPLQ0KZB1iC3ZAhCupUB8CHEek6YnXHHZBlqLLwSbHwJzhQha2uAdmT+x7HLExsHX2AztmNnDuCNdAR4ccMDI1z+M+JlfD15kVvTY6PGf8cNyGXVlZwUjrqUMZSv+LMBbF9DYuDdA4RxRVUudmibd/lQtcb0ZsVTJOIm70exQsVS/l6LmLYq4Xyt2fgPtPmvIg+WOs3leclxB0ZwQC4jFCq7Esk5F5HofHvQXnvpug39GO52+5epA2sEGdYAyTFC4YWpzLxMi2B1IiiziqroG9fBk1CjjtdCUouO21tsSQCV7wS2UkdvT7nvacW66KRo+zyXUxHXphjg1kT4KB8rXdCG+vjnRNvcq2ifPO3/hN/81OnVJGhHRVEG85BodMEIoBEEorN5bq1kdMfnQVEoZj/xd+5GIZwqgKH3TQFCj0Tv+I0nhxEeHcZMT7/FU2tGqLs2/jMAVrr9o+cVNgQ/KHVlSF5KzAJ7Zv5sLP60lAcm6IH0W22QhrZJU4mpDJlH3XLZHKxKpng1n8NmWSCzer+exzGqioGUkxiQOjz/ql589zAOha052Dgc38WkqAv0h2cIDh2m191m+ppDuB9n+yJVwCMN/2XrEbf6gbVIlihtq2kHgt0It0PM5DTWhpU3/KrbdT/J/u0Jqg0atxyGS8qMEzAeze5yo18DDdkPcMjfe/xWyFka03jTLkqC9oxIUPAfwNIG2y7cjwNhWNr7/CzUdBhFgrOETCTu/b/lqx45MyYHsWekdlAy/K35v4wD0Tb1bTIXAI7MrmWNyJ2ye1EzzwXP1OnzXSzVmckuG18SO02VWA/GdJVoESSCQEUrqTxzdSsruvVNV9UMHkz75AFoRR9m4qq6O58OEzskfxmnxV9rD9Pe1QnuBEWGILVTpaIb4+SRUtPhaDL2Zv33zRrq3oXEYUYyg5BLvBZtjmcL0HiG6sy5iuktEmeDKDZT2wwmwqsIfpx6m4d/t7zRQw+NYp3xX5uK6vgzHIqS/uZTV+wrUtjDAmA90XMnU3SH+BNWB80YGBC4poxXpS7KBfyW16+kDU9xtqbu0D06BXOh7zGl1Vq/eA+cf96wD15lQ4AFFXtH10ffboeD2V0rFt3Jq2HblXw4gEJwAvDsXo+NJioARM7tmzJTs0z4Fl35Mg5X/w/kVQ9W9OFTa6sKFdpzzr6qD1l+HbJBsbrmRsHqK1n8unoUQCq1n206ZloKmJ15fRVmJQRK0g0kX91xNgi/BQkVRylUhnAALKKHv8DkEsAcznIiHcOJSXJSor+p4z5nB8az3TAgI+dSHX7GduL5wd3QGzkoRnXjXLdPwciLv3Kaj2ytg+6UvS5rPOru4LR0jLkowpdJBVDtxvrUyZyHR8POILhPjuYEEvIu03wYgKVXdwvJdRuiKbnNnHrjY4+lNn3/qHCrcmFLCb4ijrN4Gc95jC1Q4dSr4/IdbRwsCKtigZj+FNAwrASmfmtZf5cGTYanZdNeVFSlV8R+igFFGv0SvAkWefz7SOAoql9pTmBbEx+VgKLQAC1EWMELrh4DXWdqRr+J88+BrBzzNanxNAlgI3bdq5JaOrvbyiE765bCIUT4AwOgzR9IE9CF4MZ/Txy7p46jHbHNEAgDriWuScCGKRBg+TUBv1JbjZe90Fn1WHr8gEAEHwOPJur7nR71ncYMA7eyHlHnteg8S4gaCzuuClGqqcV8fg4Mzc5GBhozYfPNscyePzohFMpXx893dR+vRZbFSu/4k4GlpUyDna/KBOWYSFlrD8cXhZT/ILY+lVSylriHZhb/zSJK9JntnXcWhb7l2yjGQDnLrcFa/WrPZw/zzQbzRp7KlHs3HBspJeQd5JrzULor6FJ+t7BVoNOze42J9cxsIz8cG0KtjyAinovVPRTP1ugEAACAOAABKCgAAAFAAAABQAAAAUAAArBQAADUwNmUzMWY2NDJlZTQxYTFmNzBmMmI1M2Q4NDc0MTAzODQ3ZDU1Y2MzNTg4ZmE0ZjEBAADXAQAABwIAAM8EAACsCAAA8AgAAAwFAADsCgAA4QYAAGAHAAAOBwAAMwsAADQAAAArBQAAMwQAAFEHAAACDQAADAMAAA8LAADrBgAAfQ0AADcNAABhCgAAiQ0AAD0EAADmAgAAkwkAANoNAABrBQAArAoAAK0EAAAdBAAAmgQAAJUJAACKDQAAMAMAAMMHAABhCQAA+AoAAC4HAACzBQAA+AMAAP4JAADDDAAA3gEAAIYJAABaBQAAJwkAAGIGAADTAgAAbwEAADoGAABHBQAAyQcAAP4EAAAQAgAAjAUAAJMCAACSAAAAxQUAAHINAABIBQAAgAUAAEoKAACyAgAA8QIAAJgGAAC0BAAAtwUAAAMBAACjDQAAoQMAACoGAADhAwAANwEAAIUJAAA4AwAAswsAAKoLAADMCAAAogsAAGwAAAB3BwAAmAIAADwJAACJDAAA6wwAAOcKAACXBAAAegsAALkLAAAwBQAAzwgAADIFAADRBwAAawAAAI0MAADJDQAAAAAAAAAAXcm26GuYyPkD1iFUY2+N2IjFxd1iVxXVxhFuPp59ItutS5Z+5GSzuzGEBQbTv27gMindMRL9Rgaz4CFZKolahLNFphfYDvIkEQqr3VEPAgeijAx1n/DIrgWPyQRCWpCnKYBSjRlWCCM1uCAIUmi0pN0atpx95OTBfPYSSEPu4LZ4BLPQvHPsS4TnP+XQwFyb2iUlKKLzKUObjxEhRL/eRtdojk2z4uZqLWoYlKX98LqadWweyLgThNlwNSxhygfDCPcOZzIO5vY9ufo42pSdnJ5Whs5TwH0fVQpyQ625CGFjU4ut7oiIi4103w682qhF0ZeNC0Q3cEd67hInmqNIgrGZ04cI76wVzflA3d9uYXlhLXAWLPscK+JEMwB2YO/pK1xcIwchPOHpeD1K9pFmC4yXxW0yMla0fKejw7ysnmwJVrKCEj1yP++OLLK+0TRQ3Cdy3ZmCtvgGpqZefHglPdnPQJ/e4vdPJYZ8owaj9KpzUuaUOb/Shp65fGqWFMJ3aO3Y7ZGTcfguI7gTBRPxYrD76imQjwQZ/LgBJWaNtxDCBDoXyQcY3uDsZRUHv4CLxhq4mimY5dfpvvDXkkySoGqnTwDX2Zep4gtIoyrtEjw4dF5L0x3mKh5hLQsVQEyUWsSpXnB/s+uCDrYeUEKt+quznx3sRstMwCXhdayLk6K9s0hkRLPLt79xyMVmyNjaszJfYKAuphP4XZ1KnD4/9XoUX5v4Sbb25+Zce3wG5fZLBsF5KzIBjgw1a+YVgBqBMtJLUWg94Wp6Z3i40CuW8Xal/pZ8xMpHEs+Pw9zP4GbM2kipupONDiRW/cK5SL3gnXhvb4GDBW0HAz+YrB6MqoUS6vXh6zfiGPBxJcUHjcgSrSVjX3HjSNTaobZU9GAGyVM9plzG/rFAjFaQEd+mAHV1hf73KEoTLqIhB0mAjiOPa1tyyeVsSq5zI9IbUjfzM6FA3W5gDaAIKXi5pfZnvvwzAKWENDaOCTpQhAV299pqLNKnQK+zL125aaBtsaS2DGPV5lGQb/1qsNMzEIiakDCcTnkjvn5Mf9BHycqVIVaVmHihoBGI2LvUPY7agB7UH9hwz2IrkkzjiGeASaUz1I99nHYQ+O4eFHW8ToDucf2ycqnxJVDzagYrvBipCI8WoWj+XeKnwyboUbvsbUPK9BZozNX6QK4+iDJw4zvWdAzkpqwY7xMbC62DCdOUBvvBX8cBQDq5GmC4v9SvN9/qEtuvCMWWsgUDTMEodzfaqYI1rYRy1DPQK4tLXy8+atLNmPX25P02KqzC/fJ3kKIo3BZReohadz2o3dw5SlLVxOHtWd1B48SD/oLWr9Y/WJnoI4EY6+9KciC4TQYh+99Z7DgRiTfD5vhRzXoFg8dlaIRCUCfQvNfxo+W8h5W9YgOifMGdLs5hJDuIxR50RO5YUuSB3aq3iC9gHuRQBl9ik2t3YgYFxzm04CvqR/W5fF72ZDSwO4NY6vKRPA/pER7bJjKy/OHnU4mqy/x+y0A5paCFWKEqsVfmH3cTc4oqys4NDyHDB398DxGpBW+G2B6xWJZtY7d7sHTZyt1p1N7+gAAtcLwfdJ0hCEiQgKvnYEVYYBrzu2cWkV2s6TuhEzoxqoRuWGdhkuP3TFpu6jDH8GqXEXPECsOYF95ecYlhfvQOXtQneQfd9cw2+m8MpqUs4/fQCgHFa5+/1UGHAyFZls1kYItboHdjuRqmjg9Ui285MYyRkHPyR/E0ZqLFVfKr3GSgpkYa0EOAbU/lH9yQaknRTtCTxgVZcRiYJjnTnGQn+rGqsuMoH19JESMjDCgVo/pZ8wchGBVeRzUtgKoJq8/qviQr5bZlqC5xSrXgNWTvg4E9eqhdBeKbS2ut+Z20P2RTOE0E6L1E8krXr8xefRHQPfsoyjEnjbhBjppLtq2PvApRjfwW65rD+IZdhFUVPcxZ3Lyhi3MyK6mTwlki+DycR/HCDO4UuVFhY9GpyAhjLFHM8Nnw7TCVHmFTgihAoQBKwD1iUTHdRSJrPyehiGGgMJYIVQdhqljBLO2MwDcoIMHQXwjJANkg7aq3ha4J85X+sVGROEv35xXkoCRCUITpQh5lzyhZ1TC752ZbqSWotzhXjA7kled113fx5F603J0mzEKPoyNtpdsh1VDkmz7uO+fr6sN1Baw27vawZvDQBgrINdviGRQV6eYxwr+WkDw4Aqh1yPDZr8eVWLQrvD7qUM5FG2ZhDCLT1QHbQ0Hrn126d2NHWzwZ7oQUTb9f6C2nRnLwUHTU1vWFofHv/GuGqLhWcETZovSRLCzgTb1ic+zSNPzN+k7n5Z9twbhJKPEZjaQGCXjnzKN9XiRI84g3Ii4FHMdut6xqbomkbN8=";
        clinkApi = new cn.ay.clinkapi.Api();
        //启动(只需要调用一次，重复调用也不会出错，调用时内部会判断是否已启动过，如果已启动过直接返回第一次调用的结果。调用时因客户端网络不通返回了0，过了段时间网络正常后我们会自动重连，这时再调用会返回150，这时如果没有调用业务也可正常运行)。
        //返回150表示成功，其它的值均为失败。150：成功、0：网络不通、1：已外部停止(如调用了停止函数，该函数一般不需要调用)、2：已内部停止(如运行过程中密钥被删除等)、170：实例到期或密钥不存在
        int ret = clinkApi.start(key);
    }

    @SuppressLint("CheckResult")
    private void setRxpermiss() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            myHandler = new MyHandler(LancherActivity.this);
                            myHandler.sendEmptyMessageDelayed(0, 2000);
                        } else {
                            showMyDialog();
                        }
                    }
                });
    }

    private void showMyDialog() {
        MyDialog myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setHintText("请在应用权限页面开启相关权限");
        myDialog.setLeftButton("退出应用", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                ActivityCollector.getActivityCollector().finishActivity(LancherActivity.class);
            }
        });
        myDialog.setRightButton("前往设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                MyUtils.getInstans().gotoAppDetailIntent(LancherActivity.this, 101);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;

        MyHandler(Activity activity) {
            mActivityReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
//                toNextActivity();
                getCall();
            }
        }
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("state", Const.IntShow.ONE);
        HttpManager.getInstance().post(Api.getVersions, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                VersionBean versionBean = new Gson().fromJson(responseString, VersionBean.class);
                String appVersion = Utils.getAppVersion(LancherActivity.this);
                if (versionBean.getCode() == 0) {
                    if (versionBean.getData().getVersions().equals(appVersion)) {
                        toNextActivity();
                    } else {
                        showVersionDialog(versionBean.getData());
                    }
                } else {
                    toNextActivity();
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                showVersionDialog("连接失败，请重试");
            }
        });
    }

    MyDialog myDialog;

    private void showVersionDialog(VersionBean.DataEntity data) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setCancelable(false);
        if (data.getState() == 1) {
            myDialog.setHintText("检测到有最新版本是否更新");
        } else if (data.getState() == 2) {
            myDialog.setHintText("检测到有最新版本请前往更新");
            myDialog.setLeftText("退出应用");
        }
        myDialog.setRightButton("前往更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                MyUtils.getInstans().toWebView(LancherActivity.this, data.getUrl());
            }
        });
        myDialog.setLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                if (data.getState() == 1) {
                    toNextActivity();
                } else if (data.getState() == 2) { //强制更新
                    ActivityCollector.getActivityCollector().finishActivity(LancherActivity.class);
                }
            }
        });
    }

    private void showVersionDialog(String msgShow) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setHintText(msgShow);
        myDialog.setLeftButton("退出应用", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                ActivityCollector.getActivityCollector().finishActivity(LancherActivity.class);
            }
        });

        myDialog.setRightButton("重新获取", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                getCall();
            }
        });
    }

    private void toNextActivity() {
        String uuid = (String) SharedPreferenceUtils.get(this, Const.User.UUID, "");
        if (StringUtils.isEmpty(uuid)) {
            SharedPreferenceUtils.put(this, Const.User.UUID, MyUtils.getInstans().getUuid(this));
        }
        if (userToken <= 0) {
            toLogin();
        } else {
            String userSig = (String) SharedPreferenceUtils.get(this, Const.User.USER_SIG, "");
            onRecvUserSig(String.valueOf(userToken), userSig);
        }
    }

    private void toMain() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Const.ShowIntent.STATE, true);
        ActivityCollector.getActivityCollector().toOtherActivity(AdvertActivity.class, bundle);
        ActivityCollector.getActivityCollector().finishActivity(LancherActivity.class);
    }

    private void toLogin() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Const.ShowIntent.STATE, false);
        ActivityCollector.getActivityCollector().toOtherActivity(AdvertActivity.class, bundle);
        ActivityCollector.getActivityCollector().finishActivity(LancherActivity.class);
    }

    private void onRecvUserSig(String userId, String userSig) {
//        showDialog();
        TUIKit.login(userId, userSig, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                /**
                 * IM 登录成功后的回调操作，一般为跳转到应用的主页（这里的主页内容为下面章节的会话列表）
                 */
                LogUtils.e(LogUtils.TAG, "登录腾讯云成功");
                dismissDialog();
                toMain();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e(LogUtils.TAG, errCode + "登录腾讯云失败" + errMsg);
                dismissDialog();
                if (errCode == 6208) { //被踢
                    showToast("您已在其他设备登录，请重新登录");
                } else if (errCode == 70001) { //用户票据过期
                    showToast("登录授权过时，请重新登录");
                }
                toLogin();
            }
        });
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            setRxpermiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
    }
}
