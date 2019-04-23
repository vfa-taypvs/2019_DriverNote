//
//  URLViewController.swift
//  CamNangNguoiLaiXe
//
//  Created by Hung Le on 3/12/17.
//  Copyright © 2017 Hung Le. All rights reserved.
//

import UIKit

class URLViewController: BaseViewController {

    var model: smallTopic?
    var scrollView: UIScrollView = {
        let sc = UIScrollView(frame: CGRect.zero)
        sc.backgroundColor = .clear
        sc.showsVerticalScrollIndicator = false
        return sc
    }()
    
    override func viewDidLoad() {
        page = "URLView"
        titlePage = model?.title
        super.viewDidLoad()
        checkRotated()
        updateUI()
        self.view.backgroundColor = UIColor(hexString: "D8D8D8")
//        setup()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(true)
        print ("TayPVS - BaoHieuViewController - viewDidAppear")
        currentOrientation = ""
        if viewHeader != nil {
            checkRotated()
        }
    }
    
    override func actionScrollTop() {
        scrollView.setContentOffset(CGPoint.zero, animated: true)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - setup
    func setup() {
        scrollView.removeSubviews()
        scrollView.frame = CGRect(x: 0, y: (viewHeader?.h)!, w: self.view.w, h: self.view.h - ((viewHeader?.h)!))
        self.view.addSubview(scrollView)
        
        var height: CGFloat = 10
        for item in (model?.content!)! {
            let name = UILabel(frame: CGRect(x: 20, y: height, w: scrollView.w - 40, h: 30))
            name.numberOfLines = 0
            name.font = UIFont.boldSystemFont(ofSize: 15)
            name.text = item.title
            name.frame = CGRect(x: name.x, y: height, w: name.w, h: name.getEstimatedHeight())
            scrollView.addSubview(name)
            let img = UIImageView(frame: CGRect(x: name.x, y: name.y + name.h + 15, w: 20, h: 20))
            img.image = #imageLiteral(resourceName: "icon_url")
            scrollView.addSubview(img)
            let des = UILabel(frame: CGRect(x: img.x + img.w + 5, y: name.y + name.h + 15, w: scrollView.w - (img.x + img.w + 5) * 2, h: 100))
            des.backgroundColor = .clear
            des.numberOfLines = 0
            des.textColor = UIColor(hexString: "585858")
            des.text = item.detail.html2String
            des.frame = CGRect(x: des.x, y: des.y, w: des.w, h: des.getEstimatedHeight())
            scrollView.addSubview(des)
            if let link = URL(string: item.detail.html2String.trimmed()) {
                des.addTapGesture(action: { (tap) in
                    UIApplication.shared.openURL(link)
                })
            }
            
            
            height = des.h + 5 + des.y
        }
        scrollView.contentSize = CGSize(width: scrollView.w, height: height + 50)
        setupAds()
    }

    override func onAfterLoadHeader () {
        btnBack?.isHidden = false
        btnTop?.isHidden = false
    }
    
    override func renderLandscape () {
        setup ()
    }
    
    override func renderPortrait () {
        setup ()
    }
}
