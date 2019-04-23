//
//  DefineViewController.swift
//  CamNangNguoiLaiXe
//
//  Created by Hung Le on 3/12/17.
//  Copyright © 2017 Hung Le. All rights reserved.
//

import UIKit

class DefineViewController: BaseViewController {
    
    var model: smallTopic?
    
    var scrollView: UIScrollView = {
        let sc = UIScrollView(frame: CGRect.zero)
        sc.backgroundColor = .clear
        sc.showsVerticalScrollIndicator = false
        return sc
    }()
    
    override func viewDidLoad() {
        page = "Define"
        titlePage = model?.title
        super.viewDidLoad()
        checkRotated()
        updateUI()
        self.view.backgroundColor = UIColor(hexString: "D8D8D8")
        print ("Scene : Define")
//        setup()
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
            let vi = UIView(frame: CGRect(x: 10, y: height, w: scrollView.w - 20, h: 20))
            vi.backgroundColor = .white
            scrollView.addSubview(vi)
            
            let name = UILabel(frame: CGRect(x: 5, y: 5, w: vi.w - 10, h: 30))
            name.numberOfLines = 0
            name.backgroundColor = .white
            name.font = UIFont.boldSystemFont(ofSize: 17)
            name.text = item.title
            name.frame = CGRect(x: name.x, y: name.y, w: name.w, h: name.getEstimatedHeight() + 20)
            vi.addSubview(name)
            
            let des = UILabel(frame: CGRect(x: name.x, y: name.y + name.h + 3, w: vi.w - name.x * 2, h: 100))
            des.backgroundColor = .clear
            des.numberOfLines = 0
            des.textColor = UIColor(hexString: "585858")
            des.text = item.detail.html2String
            des.frame = CGRect(x: des.x, y: des.y, w: des.w, h: des.getEstimatedHeight())
            vi.addSubview(des)
            
            vi.frame = CGRect(x: vi.x, y: vi.y, w: vi.w, h: des.y + des.h + 5)
            
            height = vi.h + vi.y + 10
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
