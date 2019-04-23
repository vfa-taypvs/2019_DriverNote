//
//  PDFViewController.swift
//  CamNangNguoiLaiXe
//
//  Created by Hung Le on 3/12/17.
//  Copyright © 2017 Hung Le. All rights reserved.
//

import UIKit
import WebKit

class PDFViewController: BaseViewController {

    var model: smallTopic?
    var webView: WKWebView?
    
    override func viewDidLoad() {
        page = "PDFView"
        titlePage = model?.title
        super.viewDidLoad()
        checkRotated()
        btnTop?.setImage(#imageLiteral(resourceName: "print"), for: .normal)
        updateUI()
        self.view.backgroundColor = UIColor(hexString: "D8D8D8")
//        setup()
    }
    
    override func actionScrollTop() {
        let printController = UIPrintInteractionController.shared
        let viewpf:UIViewPrintFormatter = webView!.viewPrintFormatter()
        let printInfo = UIPrintInfo(dictionary:nil)
        printInfo.outputType = UIPrintInfoOutputType.general
        printInfo.jobName = (webView?.url?.absoluteString)!
        printInfo.duplex = UIPrintInfoDuplex.none
        printInfo.orientation = UIPrintInfoOrientation.portrait
        
        //New stuff
//        printController.printPageRenderer = nil
//        printController.printingItems = nil
//        printController.printingItem = webView?.url!
        //New stuff
        printController.printFormatter = viewpf
        printController.printInfo = printInfo
        printController.showsPageRange = true
        printController.showsNumberOfCopies = true
        
//        printController.present(btnTop, animated: true, completionHandler: nil)
        printController.present(animated: true, completionHandler: nil)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - setup
    func setup() {
        if (model?.content?.count)! > 0 {
            let content = model?.content?[0]
            if let pdfURL = Bundle.main.url(forResource: content?.image, withExtension: "pdf", subdirectory: nil, localization: nil)  {
                do {
                    let data = try Data(contentsOf: pdfURL)
                    webView = WKWebView(frame: CGRect( x:0, y:(viewHeader?.h)!, width:view.w, height:view.h - (viewHeader?.h)! - 50))
                    webView?.backgroundColor = UIColor(hexString: "D8D8D8")
                    webView?.isOpaque = false
                    webView?.load(data, mimeType: "application/pdf", characterEncodingName:"", baseURL: pdfURL.deletingLastPathComponent())
                    view.addSubview(webView!)
                    
                }
                catch {
                    // catch errors here
                }
                
            }
        } else {
            let alert = UIAlertController(title: "Thông báo", message: "Không tìm thấy tập tin", preferredStyle: .alert)
            let ok = UIAlertAction(title: "Thoát", style: .cancel, handler: { (ok) in
                _ = self.navigationController?.popViewController(animated: true)
            })
            alert.addAction(ok)
            self.present(alert, animated: true, completion: nil)
        }
        setupAds()
    }
    
    override func onAfterLoadHeader () {
        btnBack?.isHidden = false
        btnTop?.isHidden = false
    }
    
    override func renderLandscape () {
        webView?.frame = CGRect( x:0, y:(viewHeader?.h)!, width:view.w, height:view.h - (viewHeader?.h)! - 50)
        setup()
    }
    
    override func renderPortrait () {
        webView?.frame = CGRect( x:0, y:(viewHeader?.h)!, width:view.w, height:view.h - (viewHeader?.h)! - 50)
        setup()
    }

}
